package com.test.gateway.service;

import com.test.gateway.api.dto.DeviceDTO;

import java.util.List;

public interface DeviceService {
    List<DeviceDTO> findAll();
    DeviceDTO findDeviceById(String uid);
    DeviceDTO addDevice(DeviceDTO deviceDto);
    DeviceDTO updateDevice(String uid, DeviceDTO deviceDto);
    void deleteDevice(String uid);
}
