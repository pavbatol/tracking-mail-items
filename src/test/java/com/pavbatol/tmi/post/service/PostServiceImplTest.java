package com.pavbatol.tmi.post.service;

import com.pavbatol.tmi.post.dto.PostDto;
import com.pavbatol.tmi.post.dto.PostDtoAddRequest;
import com.pavbatol.tmi.post.dto.PostDtoUpdate;
import com.pavbatol.tmi.post.mapper.PostMapper;
import com.pavbatol.tmi.post.model.Post;
import com.pavbatol.tmi.post.repository.PostJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class,})
class PostServiceImplTest {
    private static final int POST_CODE = 100;
    private static final String NAME = "name";
    private static final String ADDRESS = "address";

    @Mock
    private PostJpaRepository repository;
    private final PostMapper mapper = Mappers.getMapper(PostMapper.class);
    private PostServiceImpl service;

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    private Post post;

    @BeforeEach
    void setUp() {
        service = new PostServiceImpl(repository, mapper);
        post = new Post()
                .setPostCode(POST_CODE)
                .setPostName(NAME)
                .setPostAddress(ADDRESS);
    }

    @Test
    void add_ShouldCorrectMappingAndRevokeRepoForExistsAndSave() {
        PostDtoAddRequest dtoAddRequest = new PostDtoAddRequest(POST_CODE, NAME, ADDRESS);

        when(repository.existsById(anyInt())).thenReturn(false);
        when(repository.save(any())).thenReturn(post);

        service.add(dtoAddRequest);

        verify(repository, times(1)).existsById(anyInt());
        verify(repository, times(1)).save(post);

    }

    @Test
    void update_ShouldCheckPostCodeAndOnlyOneFieldIsUpdated() {
        String newName = NAME + "updated";
        PostDtoUpdate dtoUpdate = new PostDtoUpdate(newName, ADDRESS);

        when(repository.findById(anyInt())).thenReturn(Optional.of(post));

        when(repository.save(any())).thenAnswer(invocationOnMock -> {
            Post past1 = invocationOnMock.getArgument(0, Post.class);
            past1.setPostCode(post.getPostCode());
            return past1;
        });

        PostDto updated = service.update(post.getPostCode(), dtoUpdate);

        assertNotNull(updated);
        verify(repository, times(1)).save(postArgumentCaptor.capture());

        Post captorValue = postArgumentCaptor.getValue();

        assertThat(captorValue.getPostName()).isEqualTo(newName);
        assertThat(captorValue.getPostAddress()).isEqualTo(post.getPostAddress());
    }

    @Test
    void remove_shouldRevokeRepo() {
        service.remove(POST_CODE);

        verify(repository, times(1)).deleteById(POST_CODE);
    }

    @Test
    void findById_shouldRevokeRepo() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(post));

        service.findById(POST_CODE);

        verify(repository, times(1)).findById(POST_CODE);
    }

    @Test
    void findAll_shouldRevokeRepo_andCertainPageRequestAndDirectionAresPassed() {
        Integer lastPostCode = 200200;
        int pageSize = 10;
        Sort.Direction direction = Sort.Direction.ASC;
        String sortFieldName = "postCode";
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by(direction, sortFieldName));

        when(repository.findAllByPagination(anyInt(), anyString(), any(PageRequest.class)))
                .thenReturn(List.of(post));

        List<PostDto> dtos = service.findAll(lastPostCode, pageSize);

        verify(repository, times(1)).findAllByPagination(lastPostCode, direction.name(), pageRequest);
    }
}