package com.test.gateway.api.dto;

import com.test.gateway.api.dto.gateway.GatewayDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
class GatewayDtoTests {

    @Autowired
    private JacksonTester<GatewayDTO> jacksonTester;

    @Test
    void testSerialize() throws IOException {
        GatewayDTO createRequest = new GatewayDTO("123456",
                "testDevice",
                "192.168.0.0",
                new ArrayList<>(),
                null,
                null);

        JsonContent<GatewayDTO> jsonContent = jacksonTester.write(createRequest);

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.serial_number")
                .extractingJsonPathStringValue("@.serial_number").isEqualTo("123456");

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.name")
                .extractingJsonPathStringValue("@.name").isEqualTo("testDevice");

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.ip_address")
                .extractingJsonPathStringValue("@.ip_address").isEqualTo("192.168.0.0");

        assertThat(jsonContent)
                .hasJsonPathArrayValue("@.devices")
                .extractingJsonPathArrayValue("@.devices").isEmpty();

        assertThat(jsonContent)
                .hasEmptyJsonPathValue("@.created")
                .extractingJsonPathStringValue("@.created").isEqualTo(null);

        assertThat(jsonContent)
                .hasEmptyJsonPathValue("@.updated")
                .extractingJsonPathStringValue("@.updated").isEqualTo(null);
    }

    @Test
    void testDeserialize() throws IOException {
        String content = "{\n" +
                "    \"serial_number\": \"1223424\",\n" +
                "    \"name\": \"Test Device\",\n" +
                "    \"ip_address\": \"192.186.0.1\",\n" +
                "    \"devices\": null\n" +
                "}";

        GatewayDTO createRequest = jacksonTester.parseObject(content);

        assertThat(createRequest.getSerialNumber()).isEqualTo("1223424");
        assertThat(createRequest.getName()).isEqualTo("Test Device");
        assertThat(createRequest.getIpAddress()).isEqualTo("192.186.0.1");
        assertThat(createRequest.getDevices()).isEqualTo(null);
    }
}