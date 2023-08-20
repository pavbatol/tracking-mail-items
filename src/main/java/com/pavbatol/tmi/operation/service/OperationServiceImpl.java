package com.pavbatol.tmi.operation.service;

import com.pavbatol.tmi.operation.dto.OperationDto;
import com.pavbatol.tmi.operation.dto.OperationDtoAddRequest;
import com.pavbatol.tmi.operation.dto.OperationDtoUpdate;
import com.pavbatol.tmi.operation.model.enums.OperationSort;
import com.pavbatol.tmi.operation.model.filter.OperationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    @Override
    public OperationDto add(OperationDtoAddRequest dto) {
        return null;
    }

    @Override
    public OperationDto update(Long operationId, OperationDtoUpdate dto) {
        return null;
    }

    @Override
    public void remove(Long operationId) {

    }

    @Override
    public OperationDto findById(Long operationId) {
        return null;
    }

    @Override
    public List<OperationDto> findAll(OperationFilter filter,
                                      Long lastIdValue,
                                      String lastSortFieldValue,
                                      OperationSort itemSort,
                                      Sort.Direction sortDirection,
                                      Integer pageSize) {
        return null;
    }
}
