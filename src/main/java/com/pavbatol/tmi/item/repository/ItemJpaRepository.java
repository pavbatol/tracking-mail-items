package com.pavbatol.tmi.item.repository;

import com.pavbatol.tmi.item.model.Item;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemJpaRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item " +
            "where (upper(:direction) = 'ASC' and i.itemId > :lastItemId) " +
            "or (upper(:direction) = 'DESC' and i.itemId < :lastItemId) "
    )
    List<Item> findAllByPagination(Long lastItemId, String direction, PageRequest pageRequest);
}