package com.pavbatol.tmi.operation.model.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OperationSort {
    ID("id"),
    TYPE("type"),
    TIMESTAMP("operatedOn");

    private final String fieldName;

    public static OperationSort from(@NonNull String name) throws IllegalArgumentException {
        try {
            return OperationSort.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported name: " + name, e);
        }
    }
}
