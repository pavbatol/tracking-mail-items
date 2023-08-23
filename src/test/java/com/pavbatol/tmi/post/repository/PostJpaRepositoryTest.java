package com.pavbatol.tmi.post.repository;

import com.pavbatol.tmi.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PostJpaRepositoryTest {
    private final PostJpaRepository repository;

    @Test
    void findAllByPagination_shouldReturnRightSizeOfPosts_whenPaginationSpecified() {
        int lastPostId = 0;
        final String sortFieldName = "postCode";
        int pageSize = 100;
        Sort.Direction direction = Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by(direction, sortFieldName));
        long ExpectedPostTableRecordsSize = 5L;

        List<Post> found = repository.findAllByPagination(lastPostId, direction.name(), pageRequest);

        assertThat(found).isNotNull();
        assertThat(found).size().isEqualTo(ExpectedPostTableRecordsSize);

        //pagination
        lastPostId = 400400;
        long ExpectedPostSelectedRecordsSize = 1L;

        found = repository.findAllByPagination(lastPostId, direction.name(), pageRequest);

        assertThat(found).isNotNull();
        assertThat(found).size().isEqualTo(ExpectedPostSelectedRecordsSize);

        //pagination
        lastPostId = 100100;
        pageSize = 3;
        pageRequest = PageRequest.of(0, pageSize, Sort.by(direction, sortFieldName));
        ExpectedPostSelectedRecordsSize = 3L;

        found = repository.findAllByPagination(lastPostId, direction.name(), pageRequest);

        assertThat(found).isNotNull();
        assertThat(found).size().isEqualTo(ExpectedPostSelectedRecordsSize);
        assertThat(found.get(0).getPostCode()).isEqualTo(200200);

        //pagination
        lastPostId = 300300;
        pageSize = 100;
        direction = Sort.Direction.DESC;
        pageRequest = PageRequest.of(0, pageSize, Sort.by(direction, sortFieldName));
        ExpectedPostSelectedRecordsSize = 2L;

        found = repository.findAllByPagination(lastPostId, direction.name(), pageRequest);

        assertThat(found).isNotNull();
        assertThat(found).size().isEqualTo(ExpectedPostSelectedRecordsSize);
        assertThat(found.get(0).getPostCode()).isEqualTo(200200);

    }
}