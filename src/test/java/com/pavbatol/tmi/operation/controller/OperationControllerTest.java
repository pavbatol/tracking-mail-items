package com.pavbatol.tmi.operation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.operation.dto.OperationDto;
import com.pavbatol.tmi.operation.dto.OperationDtoAddRequest;
import com.pavbatol.tmi.operation.dto.OperationDtoUpdate;
import com.pavbatol.tmi.operation.model.enums.OperationSort;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.pavbatol.tmi.operation.model.filter.OperationFilter;
import com.pavbatol.tmi.operation.service.OperationService;
import com.pavbatol.tmi.post.dto.PostDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OperationController.class)
class OperationControllerTest {
    public static final String URL_TEMPLATE = "/operations";
    public static final String APPLICATION_JSON = "application/json";
    public static final int POST_CODE = 100100;
    public static final long ID = 1L;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OperationService service;

    OperationDto operationDto;
    ItemDto itemDto;
    PostDto postDto;
//    ItemType itemType = ItemType.LETTER;

    @BeforeEach
    void setUp() {
        postDto = new PostDto(POST_CODE, null, null);
        itemDto = new ItemDto(ID, null, postDto, null, null);
        operationDto = new OperationDto(ID, itemDto, postDto, OperationType.REGISTER, LocalDateTime.now());
    }

    @Test
    @SneakyThrows
    void add() {
        OperationDtoAddRequest dtoAddRequest = new OperationDtoAddRequest(itemDto, postDto, OperationType.REGISTER);

        when(service.add(dtoAddRequest)).thenReturn(operationDto);

        String content = mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAddRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(operationDto)))
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);

        verify(service, times(1)).add(dtoAddRequest);
    }

    @Test
    @SneakyThrows
    void update() {
        Long operationId = ID;
        OperationDtoUpdate dtoUpdate = new OperationDtoUpdate(itemDto, postDto, OperationType.REGISTER);

        when(service.update(operationId, dtoUpdate)).thenReturn(operationDto);

        mockMvc.perform(patch(URL_TEMPLATE + "/{operationId}", operationId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoUpdate))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(operationDto)));

        verify(service, times(1)).update(operationId, dtoUpdate);
    }

    @Test
    @SneakyThrows
    void remove() {
        long operationId = ID;

        mockMvc.perform(delete(URL_TEMPLATE + "/{operationId}", operationId)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful());

        verify(service, times(1)).remove(operationId);
    }

    @Test
    @SneakyThrows
    void findById() {
        long operationId = ID;

        when(service.findById(operationId)).thenReturn(operationDto);

        mockMvc.perform(get(URL_TEMPLATE + "/{operationId}", operationId)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(operationDto)));

        verify(service, times(1)).findById(operationId);
    }

    @Test
    @SneakyThrows
    void findAll_shouldInvokeService_whenAllParametersAreNull() {
        OperationFilter filter = new OperationFilter(null, null, null, null, null);
        Long lastIdValueExpected = null;
        String lastSortFieldValueExpected = null;
        OperationSort operationSortExpected = null;
        Sort.Direction directionExpected = Sort.Direction.valueOf("ASC");
        Integer pageSizeExpected = 10;

        List<OperationDto> operationDtos = List.of(operationDto);

        when(service.findAll(filter, lastIdValueExpected, lastSortFieldValueExpected, operationSortExpected, directionExpected, pageSizeExpected))
                .thenReturn(operationDtos);

        mockMvc.perform(get(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(operationDtos)));

        verify(service, times(1)).findAll(
                filter, lastIdValueExpected, lastSortFieldValueExpected, operationSortExpected, directionExpected, pageSizeExpected);
    }

    @Test
    @SneakyThrows
    void getItemTrack() {
        Long itemId = 1L;
        LocalDateTime start = null;
        LocalDateTime end = null;

        Sort.Direction directionExpected = Sort.Direction.ASC;
        List<OperationDto> operationDtos = List.of(operationDto);

        when(service.getItemTrack(itemId, start, end, directionExpected)).thenReturn(operationDtos);

        mockMvc.perform(get(URL_TEMPLATE + "/track/{itemId}", itemId)
                        .contentType(APPLICATION_JSON)
                        .param("itemId", String.valueOf(itemId))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(operationDtos)));

        verify(service, times(1)).getItemTrack(itemId, start, end, directionExpected);
    }
}