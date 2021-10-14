package com.test.gateway.service;

import com.test.gateway.api.dto.gateway.GatewayDTO;

import java.util.List;

public interface GatewayService {
    List<GatewayDTO> findAll();
    GatewayDTO findGatewayById(String serialNumber);
    GatewayDTO addGateway(GatewayDTO gateway);
    GatewayDTO updateGateway(String serialNumber, GatewayDTO gateway);
    void deleteGateway(String serialNumber);
}
