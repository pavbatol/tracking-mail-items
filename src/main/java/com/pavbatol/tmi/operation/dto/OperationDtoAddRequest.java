package com.pavbatol.tmi.operation.dto;

import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.pavbatol.tmi.post.dto.PostDto;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class OperationDtoAddRequest {

    @NotNull
    ItemDto item;

    @NotNull
    PostDto post;

    @NotNull
    OperationType type;
}
