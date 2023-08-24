package com.pavbatol.tmi.item.repository;

import com.pavbatol.tmi.CustomDataJpaTest;
import com.pavbatol.tmi.item.model.Item;
import com.pavbatol.tmi.item.model.enums.ItemSort;
import com.pavbatol.tmi.item.model.enums.ItemType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@CustomDataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemJpaRepositoryTest {
    private final ItemJpaRepository repository;

    @Test
    void findAllByPagination_shouldCorrectPaginating_whenAllInputArgumentsIsCorrect() {
        Long lastIdValue = 4L;
        String lastSortFieldValue = ItemType.LETTER.name();
        String sortFieldName = ItemSort.TYPE.getFieldName();
        Sort.Direction direction = Sort.Direction.ASC;
        Integer limit = 2;

        List<Item> items = repository.findAllByPagination(
                lastIdValue,
                lastSortFieldValue,
                sortFieldName,
                direction,
                limit);

        assertThat(items).isNotNull();
        assertThat(items).size().isEqualTo(2);
        assertThat(items.get(0).getId()).isEqualTo(5L);
        assertThat(items.get(0).getType()).isEqualTo(ItemType.LETTER);
        assertThat(items.get(1).getId()).isEqualTo(2L);
        assertThat(items.get(1).getType()).isEqualTo(ItemType.PARCEL);
    }

    @Test
    void findAllByPagination_shouldOnlySort_whenlastIdValueAndlastSortFieldValueAreNull() {
        Long lastIdValue = null;
        String lastSortFieldValue = null;
        String sortFieldName = ItemSort.TYPE.getFieldName();
        Sort.Direction direction = Sort.Direction.DESC;
        Integer limit = 100;

        List<Item> items = repository.findAllByPagination(
                lastIdValue,
                lastSortFieldValue,
                sortFieldName,
                direction,
                limit);

        items.forEach(System.out::println);

        assertThat(items).isNotNull();
        assertThat(items).size().isEqualTo(6);
        assertThat(items.get(5).getId()).isEqualTo(1L);
        assertThat(items.get(5).getType()).isEqualTo(ItemType.LETTER);
        assertThat(items.get(0).getId()).isEqualTo(3L);
        assertThat(items.get(0).getType()).isEqualTo(ItemType.WRAPPER);
    }
}