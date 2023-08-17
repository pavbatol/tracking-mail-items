package com.pavbatol.tmi.item.repository;

import com.pavbatol.tmi.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemJpaRepository extends JpaRepository<Item, Long> {
}