package com.pavbatol.tmi.movement.model;

import lombok.NonNull;

public enum MoveType {
    REGISTERED,
    ARRIVED,
    DEPARTED;

    public static MoveType from(@NonNull String name) throws IllegalArgumentException {
        try {
            return MoveType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported name: " + name, e);
        }
    }
}
