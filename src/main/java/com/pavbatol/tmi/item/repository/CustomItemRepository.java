package com.pavbatol.tmi.item.repository;

import com.pavbatol.tmi.item.model.Item;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CustomItemRepository {
    List<Item> findAllByPagination(Long lastIdValue,
                                   String lastSortFieldValue,
                                   String sortFieldName,
                                   Sort.Direction direction,
                                   Integer limit);
}