package com.pavbatol.tmi.operation.repository;

import com.pavbatol.tmi.operation.model.Operation;
import com.pavbatol.tmi.operation.model.QOperation;
import com.pavbatol.tmi.operation.model.enums.OperationSort;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.pavbatol.tmi.operation.model.filter.OperationFilter;
import com.pavbatol.tmi.operation.model.filter.OperationFilterHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.pavbatol.tmi.app.util.Checker.checkPaginationArguments;

@Repository
@RequiredArgsConstructor
public class CustomOperationRepositoryImpl implements CustomOperationRepository {
    public static final String ID = OperationSort.ID.getFieldName();
    private final EntityManager entityManager;

    @Override
    public List<Operation> findAllByPagination(OperationFilter filter,
                                               Long lastIdValue,
                                               String lastSortFieldValue,
                                               String sortFieldName,
                                               Sort.Direction direction,
                                               Integer limit) {
        QOperation qItem = QOperation.operation;
        BooleanBuilder builder = new BooleanBuilder();
        JPAQuery<Operation> query = new JPAQuery<>(entityManager);
        boolean onlySort = false;

        checkPaginationArguments(ID, sortFieldName, lastIdValue, lastSortFieldValue);
        if (sortFieldName == null) {
            sortFieldName = ID;
        }
        if (lastIdValue == null || lastSortFieldValue == null) {
            onlySort = true;
        }

        EnumPath<OperationType> enumPath = Expressions.enumPath(OperationType.class, qItem, sortFieldName);

        if (ID.equals(sortFieldName)) {
            query = direction == Sort.Direction.DESC
                    ? query.orderBy(qItem.id.desc())
                    : query.orderBy(qItem.id.asc());
        } else {
            query = direction == Sort.Direction.DESC
                    ? query.orderBy(enumPath.desc(), qItem.id.desc())
                    : query.orderBy(enumPath.asc(), qItem.id.asc());
        }

        if (!onlySort) {
            if (ID.equals(sortFieldName)) {
                builder.and(direction == Sort.Direction.DESC
                        ? qItem.id.lt(lastIdValue)
                        : qItem.id.gt(lastIdValue));
            } else {
                builder.and(direction == Sort.Direction.DESC
                        ? enumPath.stringValue().lt(lastSortFieldValue)
                        .or(enumPath.stringValue().eq(lastSortFieldValue).and(qItem.id.lt(lastIdValue)))
                        : enumPath.stringValue().gt(lastSortFieldValue)
                        .or(enumPath.stringValue().eq(lastSortFieldValue).and(qItem.id.gt(lastIdValue))));
            }
        }

        BooleanBuilder operationBooleanBuilder = OperationFilterHelper.getOperationBooleanBuilder(filter);
        builder.and(operationBooleanBuilder);

        if (limit != null) {
            query.limit(limit);
        }

        return query.from(qItem).where(builder).fetch();
    }
}
