package com.pavbatol.tmi.movement.model;

import lombok.NonNull;

public enum MoveType {
    ARRIVED,
    DEPARTED;

    MoveType from(@NonNull String name) throws IllegalArgumentException {
        try {
            return MoveType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported name: " + name, e);
        }
    }
}
