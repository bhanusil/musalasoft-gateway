package com.test.gateway;

import com.test.gateway.api.controller.DeviceController;
import com.test.gateway.api.controller.GatewayController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GatewayApplicationTests {

    @Autowired
    private GatewayController gatewayController;

    @Autowired
    private DeviceController deviceController;

    @Test
    void contextLoads() {
        Assertions.assertThat(gatewayController).isNotNull();
        Assertions.assertThat(deviceController).isNotNull();
    }

}
