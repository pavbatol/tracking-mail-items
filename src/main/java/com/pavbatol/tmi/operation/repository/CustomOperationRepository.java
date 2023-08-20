package com.pavbatol.tmi.operation.repository;

import com.pavbatol.tmi.operation.model.Operation;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CustomOperationRepository {
    List<Operation> findAllByPagination(Long lastIdValue,
                                        String lastSortFieldValue,
                                        String sortFieldName,
                                        Sort.Direction direction,
                                        Integer limit);
}