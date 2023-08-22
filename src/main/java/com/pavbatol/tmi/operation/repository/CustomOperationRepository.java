package com.pavbatol.tmi.operation.repository;

import com.pavbatol.tmi.operation.model.Operation;
import com.pavbatol.tmi.operation.model.filter.OperationFilter;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CustomOperationRepository {
    List<Operation> findAllByPagination(OperationFilter filter,
                                        Long lastIdValue,
                                        String lastSortFieldValue,
                                        String sortFieldName,
                                        Sort.Direction direction,
                                        Integer limit);
}