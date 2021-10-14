package com.test.gateway.service;

import com.test.gateway.api.dto.gateway.GatewayDTO;
import com.test.gateway.entity.Gateway;
import com.test.gateway.exception.EntityExistException;
import com.test.gateway.exception.EntityNotFoundException;
import com.test.gateway.mapper.GatewayMapper;
import com.test.gateway.repository.GatewayRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import(GatewayServiceImpl.class)
class GatewayServiceImplTests {

    @Autowired
    private GatewayService gatewayService;

    @MockBean
    private GatewayRepository gatewayRepository;

    private GatewayMapper mapper = Mappers.getMapper(GatewayMapper.class);

    @Test
    void testFindAllWhenThereIsNone() {
        given(gatewayRepository.findAll()).willReturn(Collections.emptyList());

        List<GatewayDTO> gatewaysFound = gatewayService.findAll();
        assertThat(gatewaysFound).isEmpty();
    }

    @Test
    void testFindAllWhenThereIsOne() {
        Gateway gateway = getDefaultGatewayEntity();
        List<Gateway> gateways = Collections.singletonList(gateway);

        given(gatewayRepository.findAll()).willReturn(gateways);

        List<GatewayDTO> gatewaysFound = gatewayService.findAll();
        assertThat(gatewaysFound).hasSize(1);
        assertThat(gatewaysFound.get(0)).isEqualTo(mapper.entityToDto(gateway));
    }


    @Test
    void testFindGatewayByIdWhenExisting() {
        Gateway gateway = getDefaultGatewayEntity();
        given(gatewayRepository.findById(anyString())).willReturn(Optional.of(gateway));

        GatewayDTO gatewayFound = gatewayService.findGatewayById(gateway.getSerialNumber());
        assertThat(gatewayFound).isEqualTo(mapper.entityToDto(gateway));
    }

    @Test
    void testAddGateway() {
        GatewayDTO gatewayDTO = getDefaultGatewayDTO();
        Gateway gatewayEntity = mapper.dtoToEntity(gatewayDTO);

        given(gatewayRepository.save(any(Gateway.class))).willReturn(gatewayEntity);

        GatewayDTO gatewayDTOSaved = gatewayService.addGateway(gatewayDTO);
        assertThat(gatewayDTOSaved.getSerialNumber()).isEqualTo(gatewayEntity.getSerialNumber());
    }

    @Test
    void testAddGatewayWithSameSerialNumber() {
        GatewayDTO gatewayDTO = getDefaultGatewayDTO();
        Gateway gatewayEntity = mapper.dtoToEntity(gatewayDTO);

        given(gatewayRepository.findById(anyString())).willReturn(Optional.of(gatewayEntity));

        Exception exception = assertThrows(EntityExistException.class, () -> {
            gatewayService.addGateway(gatewayDTO);
        });

        String expectedMessage = "was already found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdateGatewayWhenExisting() {
        GatewayDTO gatewayDTO = getDefaultGatewayDTO();
        Gateway gatewayEntity = mapper.dtoToEntity(gatewayDTO);

        given(gatewayRepository.findById(anyString())).willReturn(Optional.of(gatewayEntity));
        given(gatewayRepository.save(any(Gateway.class))).willReturn(gatewayEntity);

        GatewayDTO gatewayDTOUpdated = gatewayService.updateGateway(gatewayDTO.getSerialNumber(), gatewayDTO);
        assertThat(gatewayDTOUpdated.getSerialNumber()).isEqualTo(gatewayEntity.getSerialNumber());
    }

    @Test
    void testUpdateGatewayWhenNotExisting() {
        GatewayDTO gatewayDTO = getDefaultGatewayDTO();
        Gateway gatewayEntity = mapper.dtoToEntity(gatewayDTO);

        given(gatewayRepository.findById(anyString())).willReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            gatewayService.updateGateway(gatewayDTO.getSerialNumber(), gatewayDTO);
        });

        String expectedMessage = "was not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDeleteGatewayWhenExisting() {
        GatewayDTO gatewayDTO = getDefaultGatewayDTO();
        Gateway gatewayEntity = mapper.dtoToEntity(gatewayDTO);

        given(gatewayRepository.findById(anyString())).willReturn(Optional.of(gatewayEntity));

        gatewayService.deleteGateway(gatewayDTO.getSerialNumber());

        verify(gatewayRepository, times(1)).deleteById(eq(gatewayDTO.getSerialNumber()));
    }

    @Test
    void testDeleteGatewayWhenNotExisting() {
        GatewayDTO gatewayDTO = getDefaultGatewayDTO();
        Gateway gatewayEntity = mapper.dtoToEntity(gatewayDTO);

        given(gatewayRepository.findById(anyString())).willReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            gatewayService.deleteGateway(anyString());
        });

        String expectedMessage = "was not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private GatewayDTO getDefaultGatewayDTO() {
        GatewayDTO gatewayDTO = new GatewayDTO("123456",
                "testDevice",
                "192.168.0.0",
                new ArrayList<>(),
                new Date(),
                new Date()
        );
        return gatewayDTO;
    }

    private Gateway getDefaultGatewayEntity() {
        Gateway gateway = new Gateway("123456",
                "testDevice",
                "192.168.0.0",
                new ArrayList<>(),
                new Date(),
                new Date()
        );
        return gateway;
    }
}
