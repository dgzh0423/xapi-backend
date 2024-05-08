package com.xapi.xapigateway;

import com.xapi.xapiclientsdk.util.SignUtils;
import com.xapi.xapicommon.model.entity.InterfaceInfo;
import com.xapi.xapicommon.model.entity.User;
import com.xapi.xapicommon.service.InnerInterfaceInfoService;
import com.xapi.xapicommon.service.InnerUserInterfaceInfoService;
import com.xapi.xapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤器，对所有经过网关的请求都执行公共的逻辑
 * @author 15304
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITELIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";

    private static final Long FIVE_MINUTES = 60 * 5L;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1.用户发送请求到API网关：由客户端SDK将请求发给网关

        // 2.请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识：{}", request.getId());
        log.info("请求路径：{}", path);
        log.info("请求方法：{}", method);
        log.info("请求参数：{}", request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：{}", sourceAddress);
        log.info("请求来源地址：{}", request.getRemoteAddress());

        // 拿到响应对象
        ServerHttpResponse response = exchange.getResponse();
        // 3.访问控制-黑白名单
        if (!IP_WHITELIST.contains(sourceAddress)){
            return handleNoAuth(response);
        }

        // 4.用户鉴权，判断ak，sk是否合法
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String signature = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // 从数据库中查询accessKey是否有效
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokerUser(accessKey);
        }catch (Exception e){
            log.error("getInvokerUser error {}", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }

        // 从数据库中查询该用户的secretKey进行比较
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.generateSign(body, secretKey);
        if ( signature == null || !signature.equals(serverSign)){
            return handleNoAuth(response);
        }

        // 防重放 nonce只能用一次
        // todo 服务端要保存用过的随机数，可以用redis保存
        //  Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(nonce, "1", 1, TimeUnit.MINUTES);
        if (Long.parseLong(nonce) > 10000){
            return handleNoAuth(response);
        }

        // 时间和当前时间不能超过5分钟
        Long currentTime = System.currentTimeMillis() / 1000;
        if (currentTime - Long.parseLong(timestamp) >= FIVE_MINUTES){
            return handleNoAuth(response);
        }

        // 5.判断请求的接口是否存在; 判断是否还有调用次数
        // todo 判断请求方法是否匹配（还可以校验请求参数body）
        InterfaceInfo interfaceInfo = null;
        boolean isAvailable;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        }catch (Exception e){
            log.error("getInterfaceInfo error {}", e);
        }
        if (interfaceInfo == null){
            return handleNoAuth(response);
        }else {
            isAvailable = innerUserInterfaceInfoService.isAvailable(interfaceInfo.getId(), invokeUser.getId());
            if (!isAvailable){
                log.error("Interface is not available ! ");
                return handleNoAuth(response);
            }
        }

        // 6.请求转发，调用模拟接口
        // 问题 chain.filter()是异步操作，并非是在接口调用完成才记录响应日志，统计调用次数
        // 解决方法：使用Spring Cloud Gateway提供的自定义响应处理的装饰器
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response){
        //设置响应状态码为403 禁止访问
        response.setStatusCode(HttpStatus.FORBIDDEN);
        //返回处理完成的响应
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response){
        //设置响应状态码为500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        //返回处理完成的响应
        return response.setComplete();
    }

    /**
     * 处理响应
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行writeWith
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1，剩余调用次数 - 1 乐观锁实现
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        // 释放掉内存
                                        DataBufferUtils.release(dataBuffer);
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8);
                                        sb2.append(data);
                                        // 8.响应结果
                                        log.info("响应结果：{}", data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 9.调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 降级处理返回数据
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("Gateway handleResponse exception {}", e);
            return chain.filter(exchange);
        }
    }
}
