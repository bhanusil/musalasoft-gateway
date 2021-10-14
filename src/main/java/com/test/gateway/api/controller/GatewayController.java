package com.test.gateway.api.controller;

import com.test.gateway.api.dto.gateway.GatewayDTO;
import com.test.gateway.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/gateways")
public class GatewayController {

    @Autowired
    private GatewayService gatewayService;

    @GetMapping("")
    public List<GatewayDTO> getAllGateways() {
        return gatewayService.findAll();
    }

    @GetMapping("/{serialNumber}")
    public GatewayDTO getGatewayBySerialNumber(@PathVariable String serialNumber) {
        return gatewayService.findGatewayById(serialNumber);
    }

    @PostMapping("")
    public GatewayDTO saveCustomer(@Valid @RequestBody GatewayDTO gatewayDTO) {
        return gatewayService.addGateway(gatewayDTO);
    }

    @PutMapping("/{serialNumber}")
    public GatewayDTO updateGateway(@PathVariable String serialNumber, @Valid @RequestBody GatewayDTO gatewayDTO) {
        return gatewayService.updateGateway(serialNumber, gatewayDTO);
    }

    @DeleteMapping("/{serialNumber}")
    public ResponseEntity<HttpStatus> deleteGateway(@PathVariable String serialNumber) {
        gatewayService.deleteGateway(serialNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
