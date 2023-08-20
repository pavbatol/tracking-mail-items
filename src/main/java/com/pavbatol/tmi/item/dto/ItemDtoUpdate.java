package com.pavbatol.tmi.item.dto;

import com.pavbatol.tmi.item.model.enums.ItemType;
import com.pavbatol.tmi.post.dto.PostDto;
import lombok.Value;

import javax.validation.constraints.Pattern;

@Value
public class ItemDtoUpdate {
    ItemType type;

    PostDto post;

    @Pattern(regexp = ".*\\S.*")
    String receiverAddress;

    @Pattern(regexp = ".*\\S.*")
    String receiverName;
}
