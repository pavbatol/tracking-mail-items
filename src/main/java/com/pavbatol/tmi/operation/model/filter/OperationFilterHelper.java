package com.pavbatol.tmi.operation.model.filter;

import com.pavbatol.tmi.operation.model.QOperation;

import com.querydsl.core.BooleanBuilder;
import lombok.NonNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;


public class OperationFilterHelper {

    public static BooleanBuilder getOperationBooleanBuilder(@NonNull OperationFilter filter) {
        QOperation qInternship = QOperation.operation;
        Predicate<Object> isNullOrEmpty = isNullOrEmpty();


        return new BooleanBuilder();
    }

    private static java.util.function.Predicate<Object> isNullOrEmpty() {
        return obj ->
                Objects.isNull(obj) || (obj instanceof Collection && ((Collection<?>) obj).isEmpty());
    }
}
