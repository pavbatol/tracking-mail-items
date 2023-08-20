package com.pavbatol.tmi.operation.model;

import lombok.NonNull;

public enum OperationType {
    REGISTER,
    ARRIVE,
    DEPART;

    public static OperationType from(@NonNull String name) throws IllegalArgumentException {
        try {
            return OperationType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported name: " + name, e);
        }
    }
}
