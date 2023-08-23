package com.pavbatol.tmi.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavbatol.tmi.post.dto.PostDto;
import com.pavbatol.tmi.post.dto.PostDtoAddRequest;
import com.pavbatol.tmi.post.dto.PostDtoUpdate;
import com.pavbatol.tmi.post.service.PostService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {
    public static final String URL_TEMPLATE = "/posts";
    public static final String APPLICATION_JSON = "application/json";
    public static final int POST_CODE = 100100;
    public static final String NAME = "name";
    public static final String ADDRESS = "address";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostService service;

    PostDto postDto;

    @BeforeEach
    void setUp() {
        postDto = new PostDto(POST_CODE, NAME, ADDRESS);
    }

    @Test
    @SneakyThrows
    void add() {
        PostDtoAddRequest dtoAddRequest = new PostDtoAddRequest(POST_CODE, NAME, ADDRESS);

        when(service.add(dtoAddRequest)).thenReturn(postDto);

        String content = mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoAddRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(postDto)))
                .andReturn().getResponse().getContentAsString();

        System.out.println(content);

        verify(service, times(1)).add(dtoAddRequest);
    }

    @Test
    @SneakyThrows
    void update() {
        Integer postId = POST_CODE;
        PostDtoUpdate dtoUpdate = new PostDtoUpdate(NAME, ADDRESS);

        when(service.update(postId, dtoUpdate)).thenReturn(postDto);

        mockMvc.perform(patch(URL_TEMPLATE + "/{postId}", postId)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(postDto)));

        verify(service, times(1)).update(postId, dtoUpdate);
    }

    @Test
    @SneakyThrows
    void remove() {
        Integer postId = POST_CODE;

        mockMvc.perform(delete(URL_TEMPLATE + "/{postId}", postId)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful());

        verify(service, times(1)).remove(postId);
    }

    @Test
    @SneakyThrows
    void findById() {
        Integer postId = POST_CODE;

        when(service.findById(postId)).thenReturn(postDto);

        mockMvc.perform(get(URL_TEMPLATE + "/{postId}", postId)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(postDto)));

        verify(service, times(1)).findById(postId);
    }

    @Test
    @SneakyThrows
    void findAll() {
        Integer lastPostId = POST_CODE;
        Integer pageSize = 100;
        List<PostDto> posts = List.of(postDto);

        when(service.findAll(lastPostId, pageSize)).thenReturn(posts);

        mockMvc.perform(get(URL_TEMPLATE)
                        .contentType(APPLICATION_JSON)
                        .param("lastPostId", String.valueOf(lastPostId))
                        .param("pageSize", String.valueOf(pageSize))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(posts)));

        verify(service, times(1)).findAll(lastPostId, pageSize);
    }
}