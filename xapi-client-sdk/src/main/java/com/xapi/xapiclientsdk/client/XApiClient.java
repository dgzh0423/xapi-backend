package com.xapi.xapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.xapi.xapiclientsdk.model.User;
import java.util.HashMap;
import java.util.Map;
import static com.xapi.xapiclientsdk.util.SignUtils.generateSign;


/**
 * @author 15304
 * 调用第三方接口的客户端
 */
public class XApiClient {
    private static final String GATEWAY_HOST = "http://localhost:8090";

    private String accessKey;

    private String secretKey;

    public XApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        // 可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
    }

    //使用POST从服务器获取name
    public String getNameByPost(String name){
        // 可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.post(GATEWAY_HOST + "/api/name/", paramMap);
    }

    public String getUserNameByPost(User user){
        // 将User对象转换为JSON字符串
        String json = JSONUtil.toJsonStr(user);
        // 由客户端将请求发给网关
        HttpResponse response = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        // 返回状态码
        System.out.println(response.getStatus());
        //获取服务器返回的结果
        return response.body();
    }

    private Map<String, String> getHeaderMap(String body){
        Map<String, String> headerMap = new HashMap<>();
        // 在标准的签名认证算法中，建议至少添加以下五个参数：accessKey，secretKey，sign，nonce随机数，timestamp时间戳
        headerMap.put("accessKey", accessKey);

        // 不能直接发送secretKey ！！！密码绝不能在服务器之间传输
        //headerMap.put("secretKey", secretKey);

        // 生成随机数
        headerMap.put("nonce", RandomUtil.randomNumbers(4));
        // 用户传入的参数
        headerMap.put("body", body);
        headerMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        headerMap.put("sign", generateSign(body, secretKey));

        return headerMap;
    }

}
