package com.pavbatol.tmi.operation.service;

import com.pavbatol.tmi.operation.dto.OperationDto;
import com.pavbatol.tmi.operation.dto.OperationDtoAddRequest;
import com.pavbatol.tmi.operation.dto.OperationDtoUpdate;
import com.pavbatol.tmi.operation.model.enums.OperationSort;
import com.pavbatol.tmi.operation.model.filter.OperationFilter;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

public interface OperationService {
    OperationDto add(OperationDtoAddRequest dto);

    OperationDto update(Long operationId, OperationDtoUpdate dto);

    void remove(Long operationId);

    OperationDto findById(Long operationId);

    List<OperationDto> findAll(OperationFilter filter,
                               Long lastIdValue,
                               String lastSortFieldValue,
                               OperationSort sort,
                               Sort.Direction direction,
                               Integer limit);

    List<OperationDto> getItemTrack(Long itemId,
                                    LocalDateTime start,
                                    LocalDateTime end,
                                    Sort.Direction direction);
}
