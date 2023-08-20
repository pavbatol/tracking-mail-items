package com.pavbatol.tmi.item.model.enums;

import lombok.NonNull;

public enum ItemType {
    LETTER,
    PARCEL,
    WRAPPER,
    POSTCARD;

    public static ItemType from(@NonNull String name) throws IllegalArgumentException {
        try {
            return ItemType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported name: " + name, e);
        }
    }
}
