package com.pavbatol.tmi.operation.repository;

import com.pavbatol.tmi.operation.model.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OperationJpaRepository extends JpaRepository<Operation, Long>, CustomOperationRepository {
    List<Operation> findAllByItemIdAndOperatedOnBetween(Long itemId,
                                                        LocalDateTime start,
                                                        LocalDateTime end,
                                                        Pageable pageable);
}
