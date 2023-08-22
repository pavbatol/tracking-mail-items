package com.pavbatol.tmi.operation.model.filter;

import com.pavbatol.tmi.operation.model.QOperation;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.querydsl.core.BooleanBuilder;
import lombok.NonNull;

import java.util.Collection;
import java.util.Objects;


public class OperationFilterHelper {

    public static BooleanBuilder getOperationBooleanBuilder(@NonNull OperationFilter filter) {
        QOperation qOperation = QOperation.operation;

        return new BooleanBuilder()
                .and(!isNullOrEmpty().test(filter.getItemIds()) ? qOperation.item.id.in(filter.getItemIds()) : null)
                .and(!isNullOrEmpty().test(filter.getPostCodes()) ? qOperation.post.postCode.in(filter.getPostCodes()) : null)
                .and(!isNullOrEmpty().test(filter.getType()) ? qOperation.type.eq(OperationType.from(filter.getType())) : null)
                .and(!isNullOrEmpty().test(filter.getRangeStartOperatedOn()) ? qOperation.operatedOn.goe(filter.getRangeStartOperatedOn()) : null)
                .and(!isNullOrEmpty().test(filter.getRangeEndOperatedOn()) ? qOperation.operatedOn.loe(filter.getRangeEndOperatedOn()) : null);
    }

    private static java.util.function.Predicate<Object> isNullOrEmpty() {
        return obj ->
                Objects.isNull(obj) || (obj instanceof Collection && ((Collection<?>) obj).isEmpty());
    }
}
