package com.pavbatol.tmi.item.controller;

import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.item.dto.ItemDtoAddRequest;
import com.pavbatol.tmi.item.dto.ItemDtoUpdate;
import com.pavbatol.tmi.item.model.ItemSort;
import com.pavbatol.tmi.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Postal item", description = "API for working with Items")
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping
    @Operation(summary = "add", description = "adding a Item")
    ResponseEntity<ItemDto> add(@Valid @RequestBody ItemDtoAddRequest dto) {
        log.debug("POST (add) with dto: {}", dto);
        ItemDto body = service.add(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "update", description = "Item update")
    ResponseEntity<ItemDto> update(@PathVariable(value = "itemId") Long itemId,
                                   @Valid @RequestBody ItemDtoUpdate dto) {
        log.debug("PATCH (update) with itemId: {}, dto: {}", itemId, dto);
        ItemDto body = service.update(itemId, dto);
        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "remove", description = "deleting a Item")
    ResponseEntity<Void> remove(@PathVariable(value = "itemId") Long itemId) {
        log.debug("DELETE (remove) with itemId: {}", itemId);
        service.remove(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "findById", description = "getting a item by Id")
    public ResponseEntity<ItemDto> findById(@PathVariable("itemId") Long itemId) {
        log.debug("GET findById() with itemId {}", itemId);
        ItemDto body = service.findById(itemId);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    @Operation(summary = "findAll", description = "find all Items getting page by page")
    public ResponseEntity<List<ItemDto>> findAll(
            @PositiveOrZero() @RequestParam(value = "lastItemId", defaultValue = "0") Long lastItemId,
            @Min(1) @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sort") String sort) {
        log.debug("GET findAll() with lastItemId: {}, size: {}, sort: {}", lastItemId, pageSize, sort);

        ItemSort itemSort = sort != null ? ItemSort.from(sort) : ItemSort.ITEM_TYPE;
        List<ItemDto> body = service.findAll(lastItemId, pageSize, itemSort);
        return ResponseEntity.ok(body);
    }
}
