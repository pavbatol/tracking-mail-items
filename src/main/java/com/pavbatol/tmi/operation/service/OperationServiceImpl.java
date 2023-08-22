package com.pavbatol.tmi.operation.service;

import com.pavbatol.tmi.app.exception.ValidationException;
import com.pavbatol.tmi.app.util.Checker;
import com.pavbatol.tmi.operation.dto.OperationDto;
import com.pavbatol.tmi.operation.dto.OperationDtoAddRequest;
import com.pavbatol.tmi.operation.dto.OperationDtoUpdate;
import com.pavbatol.tmi.operation.mapper.OperationMapper;
import com.pavbatol.tmi.operation.model.Operation;
import com.pavbatol.tmi.operation.model.enums.OperationSort;
import com.pavbatol.tmi.operation.model.filter.OperationFilter;
import com.pavbatol.tmi.operation.repository.OperationJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private static final String ENTITY_SIMPLE_NAME = Operation.class.getSimpleName();
    private final OperationJpaRepository repository;
    private final OperationMapper mapper;

    @Override
    public OperationDto add(OperationDtoAddRequest dto) {
        Operation entity = mapper.toEntity(dto).setOperatedOn(LocalDateTime.now());
        Operation saved = repository.save(entity);
        log.debug("New {} saved: {}", ENTITY_SIMPLE_NAME, saved);
        return mapper.toDto(saved);
    }

    @Override
    public OperationDto update(Long operationId, OperationDtoUpdate dto) {
        Operation orig = Checker.getNonNullObject(repository, operationId);
        Operation updated = mapper.partialUpdate(dto, orig);
        Operation saved = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);
        return mapper.toDto(saved);
    }

    @Override
    public void remove(Long operationId) {
        repository.deleteById(operationId);
        log.debug("deleted {}: with id: {}", ENTITY_SIMPLE_NAME, operationId);
    }

    @Override
    public OperationDto findById(Long operationId) {
        Operation found = Checker.getNonNullObject(repository, operationId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);
        return mapper.toDto(found);
    }

    @Override
    public List<OperationDto> findAll(OperationFilter filter,
                                      Long lastIdValue,
                                      String lastSortFieldValue,
                                      OperationSort operationSort,
                                      Sort.Direction direction,
                                      Integer limit) {


        if ((lastIdValue != null || lastSortFieldValue != null) && operationSort == null) {
            throw new ValidationException("Missing 'operationSort' when specified 'lastIdValue' or 'lastSortFieldValue' argument");
        }
        if (operationSort == null) {
            operationSort = OperationSort.TYPE;
        }

        List<Operation> found = repository.findAllByPagination(
                lastIdValue,
                lastSortFieldValue,
                operationSort.getFieldName(),
                direction,
                limit);

        log.debug("Found {}s in the amount of {}, by lastIdValue: {}, lastSortFieldValue: {}, sortFieldName: {}, direction: {}, limit: {}",
                ENTITY_SIMPLE_NAME, found.size(), lastIdValue, lastSortFieldValue, operationSort.getFieldName(), direction, limit);
        return mapper.toDtos(found);
    }

    @Override
    public List<OperationDto> getItemTrack(Long itemId, LocalDateTime start, LocalDateTime end) {
        start = start == null ? LocalDateTime.of(1970, 1, 1, 0, 0, 0) : start;
        end = end == null ? LocalDateTime.now() : end;
        List<Operation> found = repository.findAllByItemIdAndOperatedOnBetween(itemId, start, end);
        log.debug("Found {}s in the amount of {}, by itemId: {}, start: {}, end: {}", ENTITY_SIMPLE_NAME, found.size(), itemId, start, end);
        return mapper.toDtos(found);
    }
}
