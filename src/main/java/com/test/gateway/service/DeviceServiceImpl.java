package com.test.gateway.service;

import com.test.gateway.api.dto.DeviceDTO;
import com.test.gateway.config.AppConfig;
import com.test.gateway.entity.Device;
import com.test.gateway.exception.EntityExistException;
import com.test.gateway.exception.EntityNotFoundException;
import com.test.gateway.exception.MaximumGatewayDeviceException;
import com.test.gateway.mapper.DeviceMapper;
import com.test.gateway.repository.DeviceRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {
    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private AppConfig appConfig;

    private DeviceMapper mapper = Mappers.getMapper(DeviceMapper.class);

    @Override
    public List<DeviceDTO> findAll() {
        return deviceRepository.findAll()
                .stream()
                .map(item -> mapper.entityToDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public DeviceDTO findDeviceById(String uid) {
        return deviceRepository.findById(uid)
                .map(t -> mapper.entityToDto(t))
                .orElseThrow(() -> new EntityNotFoundException(Device.class, "uid", uid));
    }

    @Override
    public DeviceDTO addDevice(DeviceDTO dto) {
        deviceRepository.findById(dto.getUid())
                .ifPresent(t -> {
                    throw new EntityExistException(Device.class, "uid", dto.getUid());
                });
        long existingDeviceCount = deviceRepository.countAllByGatewaySerialNumber(dto.getGatewaySerialNumber());
        if(existingDeviceCount >= 1){
            throw new MaximumGatewayDeviceException(dto.getGatewaySerialNumber(), existingDeviceCount);
        }
        return mapper.entityToDto(deviceRepository.save(mapper.dtoToEntity(dto)));
    }

    @Override
    public DeviceDTO updateDevice(String uid, DeviceDTO dto) {
        return deviceRepository.findById(uid)
            .map(t -> {
                long existingDeviceCount = deviceRepository.countAllByGatewaySerialNumber(dto.getGatewaySerialNumber());
                if(existingDeviceCount >= appConfig.getMaxDeviceCount()){
                    throw new MaximumGatewayDeviceException(dto.getGatewaySerialNumber(), existingDeviceCount);
                }
                return mapper.entityToDto(deviceRepository.save(mapper.dtoToEntity(dto)));
            })
            .orElseThrow(() -> new EntityNotFoundException(Device.class, "uid", uid));
    }

    @Override
    public void deleteDevice(String uid) {
        deviceRepository.findById(uid)
                .orElseThrow(() -> new EntityNotFoundException(Device.class, "uid", uid));
        deviceRepository.deleteById(uid);
    }

}
