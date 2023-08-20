package com.pavbatol.tmi.item.model.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemSort {
    ID("id"),
    TYPE("type"),
    NAME("receiverName"),
    ADDRESS("receiverAddress");

    private final String fieldName;

    public static ItemSort from(@NonNull String name) throws IllegalArgumentException {
        try {
            return ItemSort.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported name: " + name, e);
        }
    }
}
