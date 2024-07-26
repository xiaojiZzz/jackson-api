package com.jackson.apiinterface;

import com.jackson.apiclientsdk.client.ApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class JacksonapiInterfaceApplicationTests {

    @Resource
    private ApiClient apiClient;

    @Test
    void test1() {
    }

}
