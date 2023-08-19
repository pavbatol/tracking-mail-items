package com.pavbatol.tmi.item.service;

import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.item.dto.ItemDtoAddRequest;
import com.pavbatol.tmi.item.dto.ItemDtoUpdate;
import com.pavbatol.tmi.item.model.ItemSort;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDtoAddRequest dto);

    ItemDto update(Long itemId, ItemDtoUpdate dto);

    void remove(Long itemId);

    ItemDto findById(Long itemId);

    List<ItemDto> findAll(Long lastItemId, Integer pageSize, ItemSort itemSort);
}
