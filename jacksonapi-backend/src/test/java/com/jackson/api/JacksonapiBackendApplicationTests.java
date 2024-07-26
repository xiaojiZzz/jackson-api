package com.jackson.api;

import com.jackson.api.service.UserInterfaceInvokeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class JacksonapiBackendApplicationTests {

    @Resource
    private UserInterfaceInvokeService userInterfaceInvokeService;

    @Test
    void contextLoads() {
        boolean b = userInterfaceInvokeService.invokeCount(1L, 1L);
        Assertions.assertTrue(b);
    }

}
