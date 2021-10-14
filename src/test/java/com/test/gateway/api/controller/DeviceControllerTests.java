package com.test.gateway.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.gateway.api.dto.DeviceDTO;
import com.test.gateway.exception.EntityExistException;
import com.test.gateway.exception.EntityNotFoundException;
import com.test.gateway.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

@WebMvcTest(DeviceController.class)
class DeviceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeviceService deviceService;

    @Test
    void testGetAllDevicesWhenThereIsNone() throws Exception {
        given(deviceService.findAll()).willReturn(Collections.emptyList());

        ResultActions resultActions = mockMvc.perform(get(API_DEVICES_URL))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$, hasSize(0)));
    }

    @Test
    void testGetAllDevicesWhenThereIsOne() throws Exception {
        DeviceDTO dto = getDefaultDevice();
        List<DeviceDTO> devices = Collections.singletonList(dto);

        given(deviceService.findAll()).willReturn(devices);

        ResultActions resultActions = mockMvc.perform(get(API_DEVICES_URL))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$, hasSize(1)))
                .andExpect(jsonPath(JSON_$_0_UID, is(dto.getUid())))
                .andExpect(jsonPath(JSON_$_0_VENDOR, is(dto.getVendor())))
                .andExpect(jsonPath(JSON_$_0_STATUS, is(dto.getStatus())))
                .andExpect(jsonPath(JSON_$_0_SERIAL_NUMBER, is(dto.getGatewaySerialNumber())));
    }

    @Test
    void testGetDeviceByUidWhenNonExistent() throws Exception {
        given(deviceService.findDeviceById(anyString())).willThrow(EntityNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(get(API_DEVICES_ID_URL, "12234234"))
                .andDo(print());

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void testGetDeviceByUidWhenExistent() throws Exception {
        DeviceDTO dto = getDefaultDevice();

        given(deviceService.findDeviceById(anyString())).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(get(API_DEVICES_ID_URL, dto.getUid()))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$_UID, is(dto.getUid())))
                .andExpect(jsonPath(JSON_$_VENDOR, is(dto.getVendor())))
                .andExpect(jsonPath(JSON_$_STATUS, is(dto.getStatus())))
                .andExpect(jsonPath(JSON_$_SERIAL_NUMBER, is(dto.getGatewaySerialNumber())));
    }

    @Test
    void testCreateDeviceWhenInformingExistentUid() throws Exception {
        DeviceDTO createRequest = getDefaultCreateRequest();

        willThrow(EntityExistException.class).given(deviceService).addDevice(any(DeviceDTO.class));

        ResultActions resultActions = mockMvc.perform(post(API_DEVICES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print());

        resultActions.andExpect(status().isConflict());
    }

    @Test
    void testCreateDeviceInformingValidInfo() throws Exception {
        DeviceDTO dto = getDefaultDevice();
        given(deviceService.addDevice(any(DeviceDTO.class))).willReturn(dto);

        DeviceDTO createRequest = getDefaultCreateRequest();
        ResultActions resultActions = mockMvc.perform(post(API_DEVICES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$_UID, is(dto.getUid())))
                .andExpect(jsonPath(JSON_$_VENDOR, is(dto.getVendor())))
                .andExpect(jsonPath(JSON_$_STATUS, is(dto.getStatus())))
                .andExpect(jsonPath(JSON_$_SERIAL_NUMBER, is(dto.getGatewaySerialNumber())));
    }

    @Test
    void testUpdateDeviceWhenChangingAllFieldsOtherThanUid() throws Exception {
        DeviceDTO dto = getDefaultDevice();
        given(deviceService.updateDevice(anyString(), any(DeviceDTO.class))).willReturn(dto);

        ResultActions resultActions = mockMvc.perform(put(API_DEVICES_ID_URL, dto.getUid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(JSON_$_UID, is(dto.getUid())))
                .andExpect(jsonPath(JSON_$_VENDOR, is(dto.getVendor())))
                .andExpect(jsonPath(JSON_$_STATUS, is(dto.getStatus())))
                .andExpect(jsonPath(JSON_$_SERIAL_NUMBER, is(dto.getGatewaySerialNumber())));
    }

    @Test
    void testUpdateDeviceWhenChangingUid() throws Exception {
        DeviceDTO dto = getDefaultDevice();
        willThrow(EntityNotFoundException.class).given(deviceService).updateDevice(
                anyString(), any(DeviceDTO.class));

        ResultActions resultActions = mockMvc.perform(put(API_DEVICES_ID_URL, dto.getUid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void testDeleteDeviceWhenExistent() throws Exception {
        String id = "12312321";
        willDoNothing().given(deviceService).deleteDevice(anyString());

        ResultActions resultActions = mockMvc.perform(delete(API_DEVICES_ID_URL, id))
                .andDo(print());

        resultActions.andExpect(status().isOk());
    }

    @Test
    void testDeleteDeviceWhenNonExistent() throws Exception {
        String id = "12312321";
        willThrow(EntityNotFoundException.class).given(deviceService).deleteDevice(anyString());
        ResultActions resultActions = mockMvc.perform(delete(API_DEVICES_ID_URL, id))
                .andDo(print());

        resultActions.andExpect(status().isNotFound());
    }

    private DeviceDTO getDefaultDevice() {
        DeviceDTO deviceDTO = new DeviceDTO("123456",
                "testDevice",
                "192.168.0.0",
                "1234345",
                new Date(),
                new Date()
                );
        return deviceDTO;
    }

    public DeviceDTO getDefaultCreateRequest() {
        return new  DeviceDTO("123456",
                "testDevice",
                "192.168.0.0",
                "1234345",
                null,
                null
        );
    }

    private static final String API_DEVICES_URL = "/devices";
    private static final String API_DEVICES_ID_URL = "/devices/{id}";

    private static final String JSON_$ = "$";

    private static final String JSON_$_UID = "$.uid";
    private static final String JSON_$_VENDOR = "$.vendor";
    private static final String JSON_$_STATUS = "$.status";
    private static final String JSON_$_SERIAL_NUMBER = "$.gateway_serial_number";

    private static final String JSON_$_0_UID = "$[0].uid";
    private static final String JSON_$_0_VENDOR = "$[0].vendor";
    private static final String JSON_$_0_STATUS = "$[0].status";
    private static final String JSON_$_0_SERIAL_NUMBER = "$[0].gateway_serial_number";
}