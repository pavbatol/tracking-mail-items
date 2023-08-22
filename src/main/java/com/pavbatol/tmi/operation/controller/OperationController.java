package com.pavbatol.tmi.operation.controller;

import com.pavbatol.tmi.operation.dto.OperationDto;
import com.pavbatol.tmi.operation.dto.OperationDtoAddRequest;
import com.pavbatol.tmi.operation.dto.OperationDtoUpdate;
import com.pavbatol.tmi.operation.model.enums.OperationSort;
import com.pavbatol.tmi.operation.model.filter.OperationFilter;
import com.pavbatol.tmi.operation.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Operation of Post Office", description = "API for working with Operations")
@RequestMapping("/operations")
public class OperationController {
    private final OperationService service;

    @PostMapping
    @Operation(summary = "add", description = "adding a Operation")
    ResponseEntity<OperationDto> add(@Valid @RequestBody OperationDtoAddRequest dto) {
        log.debug("POST (add) with dto: {}", dto);
        OperationDto body = service.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{operationId}")
    @Operation(summary = "update", description = "Operation update")
    ResponseEntity<OperationDto> update(@PathVariable(value = "operationId") Long operationId,
                                        @Valid @RequestBody OperationDtoUpdate dto) {
        log.debug("PATCH (update) with operationId: {}, dto: {}", operationId, dto);
        OperationDto body = service.update(operationId, dto);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{operationId}")
    @Operation(summary = "remove", description = "deleting a Operation")
    ResponseEntity<Void> remove(@PathVariable(value = "operationId") Long operationId) {
        log.debug("DELETE (remove) with operationId: {}", operationId);
        service.remove(operationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{operationId}")
    @Operation(summary = "findById", description = "getting a Operation by Id")
    public ResponseEntity<OperationDto> findById(@PathVariable("operationId") Long operationId) {
        log.debug("GET findById() with operationId {}", operationId);
        OperationDto body = service.findById(operationId);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "findAll", description = "find all Operations getting page by page by filter and KeySet pagination")
    public ResponseEntity<List<OperationDto>> findAll(
            OperationFilter filter,
            @RequestParam(value = "lastIdValue", required = false) @PositiveOrZero() Long lastIdValue,
            @RequestParam(value = "lastSortFieldValue", required = false) String lastSortFieldValue,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "pageSize", defaultValue = "10") @Min(1) Integer pageSize) {
        log.debug("GET findAll() with filter: {}, lastItemId: {}, lastSortFieldValue: {}, sort: {}, direction: {}, pageSize: {}",
                filter, lastIdValue, lastSortFieldValue, sort, direction, pageSize);
        OperationSort operationSort = sort != null ? OperationSort.from(sort) : null;
        Sort.Direction sortDirection = Sort.Direction.valueOf(direction.toUpperCase());
        List<OperationDto> body = service.findAll(filter, lastIdValue, lastSortFieldValue, operationSort, sortDirection, pageSize);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/track/{itemId}")
    @Operation(summary = "getItemTrack", description = "find all Operations for Item by itemId and date-range")
    public ResponseEntity<List<OperationDto>> getItemTrack(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "start", required = false) LocalDateTime start,
            @RequestParam(value = "end", required = false) LocalDateTime end) {
        log.debug("GET getItemTrack() with itemId: {}, start: {}, end: {}", itemId, start, end);
        List<OperationDto> body = service.getItemTrack(itemId, start, end);
        return ResponseEntity.ok(body);
    }
}
