package com.pavbatol.tmi.item.dto;

import com.pavbatol.tmi.item.model.ItemType;
import com.pavbatol.tmi.post.dto.PostDto;
import lombok.Value;

@Value
public class ItemDto {
    Long id;
    ItemType type;
    PostDto post;
    String receiverAddress;
    String receiverName;
}
