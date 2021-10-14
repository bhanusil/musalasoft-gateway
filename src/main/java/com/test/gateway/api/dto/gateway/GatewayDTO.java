package com.test.gateway.api.dto.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.gateway.api.dto.DeviceDTO;
import com.test.gateway.api.dto.gateway.validator.IpAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@ToString
@Data
public class GatewayDTO {

    @NotBlank(message = "serial_number is Mandatory")
    @JsonProperty
    private String serialNumber;

    @NotBlank(message = "name is Mandatory")
    @JsonProperty
    private String name;

    @IpAddress
    @JsonProperty
    private String ipAddress;

    @JsonProperty
    private List<DeviceDTO> devices;

    @JsonProperty
    private Date created;

    @JsonProperty
    private Date updated;
}
