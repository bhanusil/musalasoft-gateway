package com.test.gateway.mapper;

import com.test.gateway.api.dto.DeviceDTO;
import com.test.gateway.entity.Device;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    DeviceDTO entityToDto(Device entity);

    Device dtoToEntity(DeviceDTO dto);
}
