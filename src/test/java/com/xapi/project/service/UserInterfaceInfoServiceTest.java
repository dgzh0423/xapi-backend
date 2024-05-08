package com.xapi.project.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;


@SpringBootTest
class UserInterfaceInfoServiceTest {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void invokeCount() {

        boolean b = userInterfaceInfoService.invokeCount(1L, 1L);

        //断言invokeCount方法返回true
        Assertions.assertTrue(b);
    }
}