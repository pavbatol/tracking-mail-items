package com.pavbatol.tmi.operation.repository;

import com.pavbatol.tmi.CustomDataJpaTest;
import com.pavbatol.tmi.operation.model.Operation;
import com.pavbatol.tmi.operation.model.enums.OperationSort;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.pavbatol.tmi.operation.model.filter.OperationFilter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@CustomDataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class OperationJpaRepositoryTest {
    private final OperationJpaRepository repository;

    @Test
    void findAllByPagination_shouldReturnAllRecordsAndSortedByIdAndDirectionIsAsc_whenAllArgumentsAreNull() {
        OperationFilter filter = new OperationFilter(null, null, null, null, null);
        Long lastIdValue = null;
        String lastSortFieldValue = null;
        String sortFieldName = null;
        Sort.Direction direction = null;
        Integer limit = null;

        List<Operation> operations = repository.findAllByPagination(
                filter,
                lastIdValue,
                lastSortFieldValue,
                sortFieldName,
                direction,
                limit);

        assertThat(operations).isNotNull();
        assertThat(operations).size().isEqualTo(40);
        assertThat(operations.get(0).getId()).isEqualTo(1L);
        assertThat(operations.get(0).getType()).isEqualTo(OperationType.REGISTER);
        assertThat(operations.get(39).getId()).isEqualTo(40L);
        assertThat(operations.get(39).getType()).isEqualTo(OperationType.HAND_OVER);
    }

    @Test
    void findAllByPagination_shouldCorrectPaginating_whenAllInputArgumentsIsCorrect() {
        OperationFilter filter = new OperationFilter(
                null,
                null,
                OperationType.HAND_OVER.name(),
                LocalDateTime.parse("2023-08-21T21:38:57.891"),
                LocalDateTime.parse("2023-08-21T21:57:31.205"));
        Long lastIdValue = 40L;
        String lastSortFieldValue = "HAND_OVER";
        String sortFieldName = OperationSort.TYPE.getFieldName();
        Sort.Direction direction = Sort.Direction.DESC;
        Integer limit = 2;

        List<Operation> operations = repository.findAllByPagination(
                filter,
                lastIdValue,
                lastSortFieldValue,
                sortFieldName,
                direction,
                limit);

        assertThat(operations).isNotNull();
        assertThat(operations).size().isEqualTo(2);
        assertThat(operations.get(0).getId()).isEqualTo(35L);
        assertThat(operations.get(0).getType()).isEqualTo(OperationType.HAND_OVER);
        assertThat(operations.get(1).getId()).isEqualTo(30L);
        assertThat(operations.get(1).getType()).isEqualTo(OperationType.HAND_OVER);
    }

    @Test
    void findAllByItemIdAndOperatedOnBetween_shouldReturnAllOperationsSortedByDESC_whenStartIsDeepPastAndEndIsNowAndDirectionIsDESC() {
        Long itemId = 1L;
        LocalDateTime start = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.now();
        Sort.Direction direction = Sort.Direction.DESC;
        PageRequest pageRequestExpected = PageRequest.of(
                0, Integer.MAX_VALUE, Sort.by(direction, OperationSort.TIMESTAMP.getFieldName()));

        List<Operation> operations = repository.findAllByItemIdAndOperatedOnBetween(itemId, start, end, pageRequestExpected);

        operations.forEach(System.out::println);

        assertThat(operations).isNotNull();
        assertThat(operations).size().isEqualTo(10);
        assertThat(operations.get(0).getId()).isEqualTo(15L);
        assertThat(operations.get(9).getId()).isEqualTo(1L);
    }
}
