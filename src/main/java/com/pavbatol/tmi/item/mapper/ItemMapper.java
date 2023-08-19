package com.pavbatol.tmi.item.mapper;

import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.item.dto.ItemDtoAddRequest;
import com.pavbatol.tmi.item.dto.ItemDtoUpdate;
import com.pavbatol.tmi.item.model.Item;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    Item toEntity(ItemDtoAddRequest dto);

    ItemDto toDto(Item item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item partialUpdate(ItemDtoUpdate dto, @MappingTarget Item item);

    List<ItemDto> toDtos(List<Item> items);
}
