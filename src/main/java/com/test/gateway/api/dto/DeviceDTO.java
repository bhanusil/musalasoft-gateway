package com.test.gateway.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@ToString
@Data
public class DeviceDTO {
    @JsonProperty
    private String uid;

    @JsonProperty
    private String vendor;

    @JsonProperty
    private String status;

    @JsonProperty
    private String gatewaySerialNumber;

    @JsonProperty
    private Date created;

    @JsonProperty
    private Date updated;
}
