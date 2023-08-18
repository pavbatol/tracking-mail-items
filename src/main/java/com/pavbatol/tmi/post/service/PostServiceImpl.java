package com.pavbatol.tmi.post.service;

import com.pavbatol.tmi.app.util.Checker;
import com.pavbatol.tmi.post.dto.PostDto;
import com.pavbatol.tmi.post.dto.PostDtoAddRequest;
import com.pavbatol.tmi.post.dto.PostDtoUpdate;
import com.pavbatol.tmi.post.mapper.PostMapper;
import com.pavbatol.tmi.post.model.Post;
import com.pavbatol.tmi.post.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private static final String ENTITY_SIMPLE_NAME = Post.class.getSimpleName();
    private final PostJpaRepository repository;
    private final PostMapper mapper;

    @Override
    public PostDto add(PostDtoAddRequest dto) {
        Post entity = mapper.toEntity(dto);
        Post saved = repository.save(entity);
        log.debug("New {} saved: {}", ENTITY_SIMPLE_NAME, saved);
        return mapper.toDto(saved);
    }

    @Override
    public PostDto update(Integer postId, PostDtoUpdate dto) {
        Post orig = Checker.getNonNullObject(repository, postId);
        Post updated = mapper.partialUpdate(dto, orig);
        Post saved = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);
        return mapper.toDto(saved);
    }

    @Override
    public void remove(Integer postId) {
        repository.deleteById(postId);
        log.debug("deleted {}: with id: {}", ENTITY_SIMPLE_NAME, postId);
    }

    @Override
    public PostDto findById(Integer postId) {
        Post found = Checker.getNonNullObject(repository, postId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);
        return mapper.toDto(found);
    }

    @Override
    public List<PostDto> findAll(Integer lastPostId, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, "postCode"));
//        List<Post> found = repository.findAllByPagination(lastPostId, pageRequest);
        List<Post> found = repository.findAllByPagination(lastPostId, Sort.Direction.ASC.name(), pageRequest);
//        List<Post> found = repository.findAllByPostCodeLessThan(lastPostId, pageRequest);
        log.debug("Found {}s in the amount of {}, by lastPostId: {}, pageSize: {}, direction: {}",
                ENTITY_SIMPLE_NAME, found.size(), lastPostId, pageSize, pageRequest.getSort().getOrderFor("postCode"));
        return mapper.toDtos(found);
    }
}
