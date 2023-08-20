package com.pavbatol.tmi.item.dto;

import com.pavbatol.tmi.item.model.enums.ItemType;
import com.pavbatol.tmi.post.dto.PostDto;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class ItemDtoAddRequest {
    @NotNull
    ItemType type;

    @NotNull
    PostDto post;

    @NotBlank
    String receiverAddress;

    @NotBlank
    String receiverName;
}