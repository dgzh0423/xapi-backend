package com.xapi.xapiinterface;

import com.xapi.xapiclientsdk.client.XApiClient;
import com.xapi.xapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class XapiInterfaceApplicationTests {
    @Resource
    private XApiClient xApiClient;

    @Test
    void contextLoads() {
        String name = xApiClient.getNameByGet("test");
        System.out.println(name);
        User user = new User();
        user.setUsername("X-API");
        String result = xApiClient.getUserNameByPost(user);
        System.out.println(result);
    }

}
