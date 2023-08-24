package com.pavbatol.tmi.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.item.dto.ItemDtoAddRequest;
import com.pavbatol.tmi.item.dto.ItemDtoUpdate;
import com.pavbatol.tmi.item.model.enums.ItemSort;
import com.pavbatol.tmi.item.model.enums.ItemType;
import com.pavbatol.tmi.item.service.ItemService;
import com.pavbatol.tmi.post.dto.PostDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    public static final String URL_TEMPLATE = "/items";
    public static final String APPLICATION_JSON = "application/json";
    public static final int POST_CODE = 100100;
    public static final String POST_ADDRESS = "postAddress";
    public static final String POST_NAME = "postName";
    public static final long ID = 1L;
    public static final String NAME = "name";
    public static final String ADDRESS = "address";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService service;

    PostDto postDto;
    ItemDto itemDto;
    ItemType itemType = ItemType.LETTER;

    @BeforeEach
    void setUp() {
        postDto = new PostDto(POST_CODE, POST_NAME, POST_ADDRESS);
        itemDto = new ItemDto(ID, itemType, postDto, NAME, ADDRESS);
    }

    @Test
    @SneakyThrows
    void add_shouldInvokeService() {
        Integer postCode = POST_CODE;
        ItemDtoAddRequest dtoAddRequest = new ItemDtoAddRequest(itemType, postDto, NAME, ADDRESS);

        when(service.add(postCode, dtoAddRequest)).thenReturn(itemDto);

        String content = mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAddRequest))
                        .param("postCode", String.valueOf(postCode))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)))
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);

        verify(service, times(1)).add(postCode, dtoAddRequest);
    }

    @Test
    @SneakyThrows
    void update_shouldInvokeService() {
        long itemId = ID;
        ItemDtoUpdate dtoUpdate = new ItemDtoUpdate(itemType, postDto, NAME, ADDRESS);

        when(service.update(itemId, dtoUpdate)).thenReturn(itemDto);

        mockMvc.perform(patch(URL_TEMPLATE + "/{itemId}", itemId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoUpdate))
                        .param("itemId", String.valueOf(itemId))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)))
                .andReturn().getResponse().getContentAsString();

        verify(service, times(1)).update(itemId, dtoUpdate);
    }

    @Test
    @SneakyThrows
    void remove_shouldInvokeService() {
        long itemId = ID;

        mockMvc.perform(delete(URL_TEMPLATE + "/{itemId}", itemId)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful());

        verify(service, times(1)).remove(itemId);
    }

    @Test
    @SneakyThrows
    void findById_shouldInvokeService() {
        long itemId = ID;

        when(service.findById(itemId)).thenReturn(itemDto);

        mockMvc.perform(get(URL_TEMPLATE + "/{itemId}", itemId)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));

        verify(service, times(1)).findById(itemId);
    }

    @Test
    @SneakyThrows
    void findAll_shouldInvokeService_whenAllParametersAreNull() {
        Long lastIdValueExpected = null;
        String lastSortFieldValueExpected = null;
        ItemSort itemSortExpected = null;
        Sort.Direction sortExpected = Sort.Direction.valueOf("ASC");
        Integer pageSizeExpected = 10;
        List<ItemDto> itemDtos = List.of(itemDto);

        when(service.findAll(lastIdValueExpected, lastSortFieldValueExpected, itemSortExpected, sortExpected, pageSizeExpected))
                .thenReturn(itemDtos);

        mockMvc.perform(get(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDtos)));

        verify(service, times(1)).findAll(
                lastIdValueExpected, lastSortFieldValueExpected, itemSortExpected, sortExpected, pageSizeExpected
        );
    }

    @Test
    @SneakyThrows
    void findAll_shouldInvokeService_whenAllParametersFill() {
        Long lastIdValue = 1L;
        String lastSortFieldValue = "1";
        String sort = ItemSort.ID.getFieldName();
        String direction = "DESC";
        Integer pageSize = 15;

        ItemSort itemSortExpected = ItemSort.ID;
        Sort.Direction sortExpected = Sort.Direction.DESC;
        List<ItemDto> itemDtos = List.of(itemDto);

        when(service.findAll(lastIdValue, lastSortFieldValue, itemSortExpected, sortExpected, pageSize))
                .thenReturn(itemDtos);

        mockMvc.perform(get(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .param("lastIdValue", String.valueOf(lastIdValue))
                        .param("lastSortFieldValue", String.valueOf(lastSortFieldValue))
                        .param("sort", String.valueOf(sort))
                        .param("direction", String.valueOf(direction))
                        .param("pageSize", String.valueOf(pageSize))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDtos)));

        verify(service, times(1)).findAll(
                lastIdValue, lastSortFieldValue, itemSortExpected, sortExpected, pageSize
        );
    }
}