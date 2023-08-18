package com.pavbatol.tmi.post.mapper;

import com.pavbatol.tmi.post.dto.PostDto;
import com.pavbatol.tmi.post.dto.PostDtoAddRequest;
import com.pavbatol.tmi.post.dto.PostDtoUpdate;
import com.pavbatol.tmi.post.model.Post;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {
    Post toEntity(PostDtoAddRequest postDto);

    PostDto toDto(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Post partialUpdate(PostDtoUpdate postDto, @MappingTarget Post post);

    List<PostDto> toDtos(List<Post> posts);
}