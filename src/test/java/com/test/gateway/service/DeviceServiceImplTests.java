package com.test.gateway.service;

import com.test.gateway.api.dto.DeviceDTO;
import com.test.gateway.config.AppConfig;
import com.test.gateway.entity.Device;
import com.test.gateway.exception.EntityExistException;
import com.test.gateway.exception.EntityNotFoundException;
import com.test.gateway.mapper.DeviceMapper;
import com.test.gateway.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import(DeviceServiceImpl.class)
class DeviceServiceImplTests {

    @Autowired
    private DeviceService deviceService;

    @MockBean
    private DeviceRepository deviceRepository;

    @MockBean
    private AppConfig appConfig;

    private DeviceMapper mapper = Mappers.getMapper(DeviceMapper.class);

    @Test
    void testFindAllWhenThereIsNone() {
        given(deviceRepository.findAll()).willReturn(Collections.emptyList());

        List<DeviceDTO> gatewaysFound = deviceService.findAll();
        assertThat(gatewaysFound).isEmpty();
    }

    @Test
    void testFindAllWhenThereIsOne() {
        Device device = getDefaultDeviceEntity();
        List<Device> devices = Collections.singletonList(device);

        given(deviceRepository.findAll()).willReturn(devices);

        List<DeviceDTO> devicesFound = deviceService.findAll();
        assertThat(devicesFound).hasSize(1);
        assertThat(devicesFound.get(0)).isEqualTo(mapper.entityToDto(device));
    }


    @Test
    void testFindDeviceByIdWhenExisting() {
        Device device = getDefaultDeviceEntity();
        given(deviceRepository.findById(anyString())).willReturn(Optional.of(device));

        DeviceDTO deviceFound = deviceService.findDeviceById(device.getUid());
        assertThat(deviceFound).isEqualTo(mapper.entityToDto(device));
    }

    @Test
    void testAddDevice() {
        DeviceDTO deviceDTO = getDefaultDeviceDTO();
        Device deviceEntity = mapper.dtoToEntity(deviceDTO);

        given(deviceRepository.save(any(Device.class))).willReturn(deviceEntity);

        DeviceDTO deviceDtoSaved = deviceService.addDevice(deviceDTO);
        assertThat(deviceDtoSaved.getUid()).isEqualTo(deviceEntity.getUid());
    }

    @Test
    void testAddDeviceWithSameUid() {
        DeviceDTO deviceDTO = getDefaultDeviceDTO();
        Device deviceEntity = mapper.dtoToEntity(deviceDTO);

        given(deviceRepository.findById(anyString())).willReturn(Optional.of(deviceEntity));

        Exception exception = assertThrows(EntityExistException.class, () -> {
            deviceService.addDevice(deviceDTO);
        });

        String expectedMessage = "was already found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateDeviceWhenExisting() {
        DeviceDTO deviceDTO = getDefaultDeviceDTO();
        Device deviceEntity = mapper.dtoToEntity(deviceDTO);

        given(appConfig.getMaxDeviceCount()).willReturn(1L);
        given(deviceRepository.findById(anyString())).willReturn(Optional.of(deviceEntity));
        given(deviceRepository.save(any(Device.class))).willReturn(deviceEntity);

        DeviceDTO deviceDtoUpdated = deviceService.updateDevice(deviceDTO.getUid(), deviceDTO);
        assertThat(deviceDtoUpdated.getUid()).isEqualTo(deviceEntity.getUid());
    }

    @Test
    void testUpdateDeviceWhenNotExisting() {
        DeviceDTO deviceDTO = getDefaultDeviceDTO();
        Device deviceEntity = mapper.dtoToEntity(deviceDTO);

        given(deviceRepository.findById(anyString())).willReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            deviceService.updateDevice(deviceDTO.getUid(), deviceDTO);
        });

        String expectedMessage = "was not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeleteDeviceWhenExisting() {
        DeviceDTO deviceDTO = getDefaultDeviceDTO();
        Device deviceEntity = mapper.dtoToEntity(deviceDTO);

        given(deviceRepository.findById(anyString())).willReturn(Optional.of(deviceEntity));

        deviceService.deleteDevice(deviceDTO.getUid());

        verify(deviceRepository, times(1)).deleteById(eq(deviceDTO.getUid()));
    }

    @Test
    void testDeleteDeviceWhenNotExisting() {
        given(deviceRepository.findById(anyString())).willReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            deviceService.deleteDevice(anyString());
        });

        String expectedMessage = "was not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private DeviceDTO getDefaultDeviceDTO() {
        DeviceDTO deviceDTO = new DeviceDTO("123456",
                "testDevice",
                "192.168.0.0",
                "1234345",
                new Date(),
                new Date()
        );
        return deviceDTO;
    }

    private Device getDefaultDeviceEntity() {
        Device device = new Device("123456",
                "testDevice",
                "192.168.0.0",
                "123456",
                new Date(),
                new Date()
        );
        return device;
    }
}
