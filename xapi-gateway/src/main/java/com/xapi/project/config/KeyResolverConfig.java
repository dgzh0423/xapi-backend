package com.xapi.project.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author 15304
 *
 */
@Configuration
public class KeyResolverConfig {

    @Bean
    public KeyResolver apiKeyResolver() {
        // 按URL限流,即以每秒内请求数按URL分组统计，超出限流的url请求都将返回429状态
        return exchange -> Mono.just(exchange.getRequest().getPath().toString());
        // 按调用接口的用户限流
        //return exchange -> Mono.just(exchange.getRequest().getHeaders().getFirst("accessKey"));
    }

}

