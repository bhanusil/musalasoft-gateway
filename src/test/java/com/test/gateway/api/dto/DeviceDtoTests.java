package com.test.gateway.api.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
class DeviceDtoTests {

    @Autowired
    private JacksonTester<DeviceDTO> jacksonTester;

    @Test
    void testSerialize() throws IOException {
        DeviceDTO request = new DeviceDTO("23874993248234823944823492835",
                "Samsung",
                "Online",
                "1223123",
                null,
                null);

        JsonContent<DeviceDTO> jsonContent = jacksonTester.write(request);

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.uid")
                .extractingJsonPathStringValue("@.uid").isEqualTo("23874993248234823944823492835");

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.vendor")
                .extractingJsonPathStringValue("@.vendor").isEqualTo("Samsung");

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.status")
                .extractingJsonPathStringValue("@.status").isEqualTo("Online");

        assertThat(jsonContent)
                .hasJsonPathStringValue("@.gateway_serial_number")
                .extractingJsonPathStringValue("@.gateway_serial_number").isEqualTo("1223123");

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
                "        \"uid\": \"23874993248234823944823492835\",\n" +
                "        \"vendor\": \"Samsung\",\n" +
                "        \"status\": \"ONLINE\",\n" +
                "        \"gateway_serial_number\": \"1223424\"\n" +
                "    }";

        DeviceDTO request = jacksonTester.parseObject(content);

        assertThat(request.getUid()).isEqualTo("23874993248234823944823492835");
        assertThat(request.getVendor()).isEqualTo("Samsung");
        assertThat(request.getStatus()).isEqualTo("ONLINE");
        assertThat(request.getGatewaySerialNumber()).isEqualTo("1223424");
    }
}