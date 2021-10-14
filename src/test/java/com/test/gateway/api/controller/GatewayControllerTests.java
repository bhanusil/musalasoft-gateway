package com.test.gateway.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.gateway.api.dto.gateway.GatewayDTO;
import com.test.gateway.exception.EntityExistException;
import com.test.gateway.exception.EntityNotFoundException;
import com.test.gateway.service.GatewayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GatewayController.class)
class GatewayControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GatewayService gatewayService;

    @Test
    void testGetAllGatewaysWhenThereIsNone() throws Exception {
        given(gatewayService.findAll()).willReturn(Collections.emptyList());

        ResultActions resultActions = mockMvc.perform(get(API_GATEWAYS_URL))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$, hasSize(0)));
    }

    @Test
    void testGetAllGatewaysWhenThereIsOne() throws Exception {
        GatewayDTO dto = getDefaultGateway();
        List<GatewayDTO> gateways = Collections.singletonList(dto);

        given(gatewayService.findAll()).willReturn(gateways);

        ResultActions resultActions = mockMvc.perform(get(API_GATEWAYS_URL))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$, hasSize(1)))
                .andExpect(jsonPath(JSON_$_0_SERIAL_NUMBER, is(dto.getSerialNumber())))
                .andExpect(jsonPath(JSON_$_0_NAME, is(dto.getName())))
                .andExpect(jsonPath(JSON_$_0_IP_ADDRESS, is(dto.getIpAddress())));
    }

    @Test
    void testGetGatewayBySerialNumberWhenNonExistent() throws Exception {
        given(gatewayService.findGatewayById(anyString())).willThrow(EntityNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(get(API_GATEWAYS_ID_URL, "12234234"))
                .andDo(print());

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void testGetGatewayBySerialNumberWhenExistent() throws Exception {
        GatewayDTO dto = getDefaultGateway();

        given(gatewayService.findGatewayById(anyString())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(get(API_GATEWAYS_ID_URL, dto.getSerialNumber()))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$_SERIAL_NUMBER, is(dto.getSerialNumber())))
                .andExpect(jsonPath(JSON_$_NAME, is(dto.getName())))
                .andExpect(jsonPath(JSON_$_IP_ADDRESS, is(dto.getIpAddress())));
    }

    @Test
    void testCreateGatewayWhenInformingExistentGatewaySerial() throws Exception {
        GatewayDTO createRequest = getDefaultCreateRequest();

        willThrow(EntityExistException.class).given(gatewayService).addGateway(any(GatewayDTO.class));

        ResultActions resultActions = mockMvc.perform(post(API_GATEWAYS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print());

        resultActions.andExpect(status().isConflict());
    }

    @Test
    void testCreateGatewayInformingValidInfo() throws Exception {
        GatewayDTO dto = getDefaultGateway();
        given(gatewayService.addGateway(any(GatewayDTO.class))).willReturn(dto);

        GatewayDTO createRequest = getDefaultCreateRequest();
        ResultActions resultActions = mockMvc.perform(post(API_GATEWAYS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$_SERIAL_NUMBER, is(dto.getSerialNumber())))
                .andExpect(jsonPath(JSON_$_NAME, is(dto.getName())))
                .andExpect(jsonPath(JSON_$_IP_ADDRESS, is(dto.getIpAddress())))
                .andExpect(jsonPath(JSON_$_DEVICES, is(dto.getDevices())));
    }

    @Test
    void testCreateGatewayWithInvalidInfo() throws Exception {
        GatewayDTO dto = getDefaultGateway();
        given(gatewayService.addGateway(any(GatewayDTO.class))).willReturn(dto);

        GatewayDTO createRequest = getInvalidCreateRequest();
        ResultActions resultActions = mockMvc.perform(post(API_GATEWAYS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print());

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateGatewayWhenChangingAllFieldsOtherThanSerialNumber() throws Exception {
        GatewayDTO dto = getDefaultGateway();
        given(gatewayService.updateGateway(anyString(), any(GatewayDTO.class))).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(put(API_GATEWAYS_ID_URL, dto.getSerialNumber())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$_SERIAL_NUMBER, is(dto.getSerialNumber())))
                .andExpect(jsonPath(JSON_$_NAME, is(dto.getName())))
                .andExpect(jsonPath(JSON_$_IP_ADDRESS, is(dto.getIpAddress())))
                .andExpect(jsonPath(JSON_$_DEVICES, is(dto.getDevices())));
    }

    @Test
    void testUpdateGatewayWhenChangingSerialNumber() throws Exception {
        GatewayDTO dto = getDefaultGateway();
        willThrow(EntityNotFoundException.class).given(gatewayService).updateGateway(
                anyString(), any(GatewayDTO.class));

        ResultActions resultActions = mockMvc.perform(put(API_GATEWAYS_ID_URL, dto.getSerialNumber())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void testDeleteGatewayWhenExistent() throws Exception {
        String id = "12312321";
        willDoNothing().given(gatewayService).deleteGateway(anyString());

        ResultActions resultActions = mockMvc.perform(delete(API_GATEWAYS_ID_URL, id))
                .andDo(print());

        resultActions.andExpect(status().isOk());
    }

    @Test
    void testDeleteGatewayWhenNonExistent() throws Exception {
        String id = "12312321";
        willThrow(EntityNotFoundException.class).given(gatewayService).deleteGateway(anyString());
        ResultActions resultActions = mockMvc.perform(delete(API_GATEWAYS_ID_URL, id))
                .andDo(print());

        resultActions.andExpect(status().isNotFound());
    }

    private GatewayDTO getDefaultGateway() {
        GatewayDTO gatewayDTO = new GatewayDTO("123456",
                "testDevice",
                "192.168.0.0",
                new ArrayList<>(),
                new Date(),
                new Date()
                );
        return gatewayDTO;
    }

    public GatewayDTO getDefaultCreateRequest() {
        return new  GatewayDTO("123456",
                "testDevice",
                "192.168.0.0",
                new ArrayList<>(),
                null,
                null
        );
    }

    public GatewayDTO getInvalidCreateRequest() {
        return new  GatewayDTO("123456",
                "testDevice",
                "192.899.0.0",
                new ArrayList<>(),
                null,
                null
        );
    }

    private static final String API_GATEWAYS_URL = "/gateways";
    private static final String API_GATEWAYS_ID_URL = "/gateways/{id}";
    private static final String API_GATEWAYS_USERNAME_USERNAME_URL = "/api/users/username/{username}";

    private static final String JSON_$ = "$";

    private static final String JSON_$_SERIAL_NUMBER = "$.serial_number";
    private static final String JSON_$_NAME = "$.name";
    private static final String JSON_$_IP_ADDRESS = "$.ip_address";
    private static final String JSON_$_DEVICES = "$.devices";

    private static final String JSON_$_0_SERIAL_NUMBER = "$[0].serial_number";
    private static final String JSON_$_0_NAME = "$[0].name";
    private static final String JSON_$_0_IP_ADDRESS = "$[0].ip_address";
}