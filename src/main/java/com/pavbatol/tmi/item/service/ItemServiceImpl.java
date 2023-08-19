package com.pavbatol.tmi.item.service;

import com.pavbatol.tmi.app.util.Checker;
import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.item.dto.ItemDtoAddRequest;
import com.pavbatol.tmi.item.dto.ItemDtoUpdate;
import com.pavbatol.tmi.item.mapper.ItemMapper;
import com.pavbatol.tmi.item.model.Item;
import com.pavbatol.tmi.item.model.ItemSort;
import com.pavbatol.tmi.item.repository.ItemJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final String ENTITY_SIMPLE_NAME = Item.class.getSimpleName();
    private final ItemJpaRepository repository;
    private final ItemMapper mapper;

    @Override
    public ItemDto add(ItemDtoAddRequest dto) {
        Item entity = mapper.toEntity(dto);
        Item saved = repository.save(entity);
        log.debug("New {} saved: {}", ENTITY_SIMPLE_NAME, saved);
        return mapper.toDto(saved);
    }

    @Override
    public ItemDto update(Long itemId, ItemDtoUpdate dto) {
        Item orig = Checker.getNonNullObject(repository, itemId);
        Item updated = mapper.partialUpdate(dto, orig);
        Item saved = repository.save(updated);
        log.debug("Updated {}: {}", ENTITY_SIMPLE_NAME, updated);
        return mapper.toDto(saved);
    }

    @Override
    public void remove(Long itemId) {
        repository.deleteById(itemId);
        log.debug("deleted {}: with id: {}", ENTITY_SIMPLE_NAME, itemId);
    }

    @Override
    public ItemDto findById(Long itemId) {
        Item found = Checker.getNonNullObject(repository, itemId);
        log.debug("Found {}: {}", ENTITY_SIMPLE_NAME, found);
        return mapper.toDto(found);
    }

    @Override
//    public List<ItemDto> findAll(Long lastItemId, Integer pageSize, ItemSort itemSort, Sort.Direction sortDirection) {
    public List<ItemDto> findAll(Long lastIdValue,
                                 String lastSortFieldValue,
                                 ItemSort itemSort,
                                 Sort.Direction direction,
                                 Integer limit) {
        String sortFieldName;
        switch (itemSort) {
            case ID:
                sortFieldName = "id";
                break;
            case TYPE:
                sortFieldName = "type";
                break;
            case NAME:
                sortFieldName = "receiverName";
                break;
            case ADDRESS:
                sortFieldName = "receiverAddress";
                break;
            default:
                throw new IllegalArgumentException("Unsupported ItemSort value" + itemSort);
        }
//        Sort.Direction direction = Sort.Direction.ASC;
//        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by(direction, sortingField));
//        List<Item> found = repository.findAllByPagination(lastItemId, direction.name(), pageRequest);

//        List<Item> found = repository.findAllByPagination_2(lastItemId, direction.name(), sortingField, 2);

//        List<Item> found = repository.findAllByPagination_2(lastItemId, "ASC", "item_id",  pageSize);
//        List<Item> found = repository.findAllByPagination_2(lastItemId, sortDirection.name(), "item_id",  pageSize);
//        List<Item> found = repository.findAllByPagination_2(lastItemId, sortDirection.name(),  pageSize);


        List<Item> found = repository.findAllByPagination_3(
                lastIdValue,
                lastSortFieldValue,
                sortFieldName,
                direction,
                limit);

//        log.debug("Found {}s in the amount of {}, by lastItemId: {}, pageSize: {}, direction: {}",
//                ENTITY_SIMPLE_NAME, found.size(), lastItemId, pageSize, sortDirection);
        log.debug("Found {}s in the amount of {}, by lastIdValue: {}, lastSortFieldValue: {}, sortFieldName: {}, direction: {}, limit: {}",
                ENTITY_SIMPLE_NAME, found.size(), lastIdValue, lastSortFieldValue, sortFieldName, direction, limit);
        return mapper.toDtos(found);
    }
}
