package com.pavbatol.tmi.operation.dto;

import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.pavbatol.tmi.post.dto.PostDto;
import lombok.Value;

@Value
public class OperationDtoUpdate {
    ItemDto item;
    PostDto post;
    OperationType type;
}
