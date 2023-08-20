package com.pavbatol.tmi.operation.dto;

import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.pavbatol.tmi.post.dto.PostDto;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class OperationDto {
    Long id;
    ItemDto item;
    PostDto post;
    OperationType type;
    LocalDateTime operatedOn;
}
