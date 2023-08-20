package com.pavbatol.tmi.operation.repository;

import com.pavbatol.tmi.operation.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationJpaRepository extends JpaRepository<Operation, Long> {
}
