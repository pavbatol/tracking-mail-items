package com.pavbatol.tmi.item.repository;

import com.pavbatol.tmi.item.model.Item;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemJpaRepository extends JpaRepository<Item, Long>, CustomItemRepository {

//    @Query("select i from Item i " +
//            "where (UPPER(:direction) = 'ASC' and i.id > :lastId) " +
//            "   or (UPPER(:direction) = 'DESC' and i.id < :lastId)")
//    List<Item> findAllByPagination(Long lastId, String direction, PageRequest pageRequest);
//
//    @Query(value = "SELECT * FROM items " +
//            "WHERE (:direction = 'ASC' AND item_id > :lastId) " +
//            "   OR (:direction = 'DESC' AND item_id < :lastId) " +
//            "ORDER BY CASE WHEN :direction = 'ASC2' THEN item_id END ASC , " +
//            "         CASE WHEN :direction = 'DESC2' THEN item_id END DESC " +
//            "LIMIT :limit",
//            nativeQuery = true)
//    List<Item> findAllByPagination_2(Long lastId, String direction, Integer limit);

}