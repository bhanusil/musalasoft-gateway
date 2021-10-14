package com.test.gateway.repository;

import com.test.gateway.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, String> {
    long countAllByGatewaySerialNumber(String gatewaySerialNumber);
}
