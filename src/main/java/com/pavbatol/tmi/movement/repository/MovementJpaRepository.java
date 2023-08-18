package com.pavbatol.tmi.movement.repository;

import com.pavbatol.tmi.movement.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementJpaRepository extends JpaRepository<Movement, Long> {
}
