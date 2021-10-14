package com.test.gateway.api.controller;

import com.test.gateway.api.dto.DeviceDTO;
import com.test.gateway.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("")
    public List<DeviceDTO> getAllGateways() {
        return deviceService.findAll();
    }

    @GetMapping("/{uid}")
    public DeviceDTO getGatewayBySerialNumber(@PathVariable String uid) {
        return deviceService.findDeviceById(uid);
    }

    @PostMapping("")
    public DeviceDTO saveCustomer(@Valid @RequestBody DeviceDTO dto) {
        return deviceService.addDevice(dto);
    }

    @PutMapping("/{uid}")
    public DeviceDTO updateGateway(@PathVariable String uid, @Valid @RequestBody DeviceDTO dto) {
        return deviceService.updateDevice(uid, dto);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<HttpStatus> deleteGateway(@PathVariable String uid) {
        deviceService.deleteDevice(uid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
