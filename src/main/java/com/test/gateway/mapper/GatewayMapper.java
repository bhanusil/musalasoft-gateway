package com.test.gateway.mapper;

import com.test.gateway.api.dto.gateway.GatewayDTO;
import com.test.gateway.entity.Gateway;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GatewayMapper {
    GatewayDTO entityToDto(Gateway entity);
    Gateway dtoToEntity(GatewayDTO gatewayDTO);
}
