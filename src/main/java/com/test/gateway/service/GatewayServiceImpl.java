package com.test.gateway.service;

import com.test.gateway.api.dto.gateway.GatewayDTO;
import com.test.gateway.entity.Gateway;
import com.test.gateway.exception.EntityExistException;
import com.test.gateway.exception.EntityNotFoundException;
import com.test.gateway.mapper.GatewayMapper;
import com.test.gateway.repository.GatewayRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GatewayServiceImpl implements GatewayService {
    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    @Autowired
    private GatewayRepository gatewayRepository;

    private GatewayMapper mapper = Mappers.getMapper(GatewayMapper.class);

    @Override
    public List<GatewayDTO> findAll() {
        return gatewayRepository.findAll()
                .stream()
                .map(item -> mapper.entityToDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public GatewayDTO findGatewayById(String serialNumber) {
        return gatewayRepository.findById(serialNumber)
                .map(t -> mapper.entityToDto(t))
                .orElseThrow(() -> new EntityNotFoundException(Gateway.class, "serialNumber", serialNumber));
    }

    @Override
    public GatewayDTO addGateway(GatewayDTO dto) {
        gatewayRepository.findById(dto.getSerialNumber())
                .ifPresent(t -> {
                    throw new EntityExistException(Gateway.class, "serialNumber", dto.getSerialNumber());
                });
        return mapper.entityToDto(gatewayRepository.save(mapper.dtoToEntity(dto)));
    }

    @Override
    public GatewayDTO updateGateway(String serialNumber, GatewayDTO dto) {
        return gatewayRepository.findById(serialNumber)
                .map(t -> mapper.entityToDto(gatewayRepository.save(mapper.dtoToEntity(dto))))
                .orElseThrow(() -> new EntityNotFoundException(Gateway.class, "serialNumber", serialNumber));
    }

    @Override
    public void deleteGateway(String serialNumber) {
        gatewayRepository.findById(serialNumber)
                .orElseThrow(() -> new EntityNotFoundException(Gateway.class, "serialNumber", serialNumber));
        gatewayRepository.deleteById(serialNumber);
    }

}
