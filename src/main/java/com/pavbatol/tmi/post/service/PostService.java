package com.pavbatol.tmi.post.service;

import com.pavbatol.tmi.post.dto.PostDtoAddRequest;
import com.pavbatol.tmi.post.dto.PostDto;
import com.pavbatol.tmi.post.dto.PostDtoUpdate;

import java.util.List;

public interface PostService {
    PostDto add(PostDtoAddRequest dto);

    PostDto update(PostDtoUpdate dto);

    void remove(Integer postId);

    PostDto findById(Long postId);

    List<PostDto> findAll(Long lastPostId, Integer size);
}
