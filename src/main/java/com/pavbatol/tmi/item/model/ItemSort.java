package com.pavbatol.tmi.item.model;

import lombok.NonNull;

public enum ItemSort {
    ID,
    TYPE,
    NAME,
    ADDRESS;

    public static ItemSort from(@NonNull String name) throws IllegalArgumentException {
        try {
            return ItemSort.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported name: " + name, e);
        }
    }
}
