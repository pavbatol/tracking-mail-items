package com.pavbatol.tmi.operation.repository;

import com.pavbatol.tmi.app.exception.ValidationException;
import com.pavbatol.tmi.operation.model.Operation;
import com.pavbatol.tmi.operation.model.QOperation;
import com.pavbatol.tmi.operation.model.enums.OperationSort;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomOperationRepositoryImpl implements CustomOperationRepository {
    public static final String ID = OperationSort.ID.getFieldName();
    private final EntityManager entityManager;
    private boolean onlySort;

    @Override
    public List<Operation> findAllByPagination(Long lastIdValue,
                                               String lastSortFieldValue,
                                               String sortFieldName,
                                               Sort.Direction direction,
                                               Integer limit) {
        QOperation qItem = QOperation.operation;
        BooleanBuilder builder = new BooleanBuilder();
        JPAQuery<Operation> query = new JPAQuery<>(entityManager);

        checkAndCorrectArguments(lastIdValue, lastSortFieldValue, sortFieldName);
        if (lastIdValue == null || lastSortFieldValue == null) {
            onlySort = true;
        }

//        EnumPath<OperationSort> enumPath = Expressions.enumPath(OperationSort.class, qItem, sortFieldName);
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
//                if (direction == Sort.Direction.DESC) {
//                    builder.and(qItem.id.lt(lastIdValue));
//                } else {
//                    builder.and(qItem.id.gt(lastIdValue));
//                }
                builder.and(direction == Sort.Direction.DESC
                        ? qItem.id.lt(lastIdValue)
                        : qItem.id.gt(lastIdValue));
            } else {
//                if (direction == Sort.Direction.DESC) {
//                    builder.and(enumPath.stringValue().lt(lastSortFieldValue)
//                            .or(enumPath.stringValue().eq(lastSortFieldValue).and(qItem.id.lt(lastIdValue))));
//                } else {
//                    builder.and(enumPath.stringValue().gt(lastSortFieldValue)
//                            .or(enumPath.stringValue().eq(lastSortFieldValue).and(qItem.id.gt(lastIdValue))));
//                }
                builder.and(direction == Sort.Direction.DESC
                        ? enumPath.stringValue().lt(lastSortFieldValue)
                        .or(enumPath.stringValue().eq(lastSortFieldValue).and(qItem.id.lt(lastIdValue)))
                        : enumPath.stringValue().gt(lastSortFieldValue)
                        .or(enumPath.stringValue().eq(lastSortFieldValue).and(qItem.id.gt(lastIdValue))));
            }
        }

        if (limit != null) {
            query.limit(limit);
        }

        return query.from(qItem).where(builder).fetch();
    }

    private void checkAndCorrectArguments(Long lastIdValue, String lastSortFieldValue, String sortFieldName) {
        if (!(lastIdValue == null && lastSortFieldValue == null)
                && !(lastIdValue != null && lastSortFieldValue != null && sortFieldName != null)) {
            throw new ValidationException("Specify arguments 'lastIdValue', 'lastSortFieldValue', 'sortFieldName'");
        }
        if (sortFieldName == null) {
            sortFieldName = ID;
        }
    }
}

/*

        SimplePath<Long> enumPath = Expressions.path(Long.class, qItem, sortFieldName);
//        SimplePath<String> enumPath = Expressions.path(String.class, qItem, sortFieldName);
//        SimplePath<?> enumPath = Expressions.path(ItemSort.ID.getClazz(), qItem, sortFieldName);

        String fieldName = "id"; // здесь должно быть имя поля, по которому нужно сортировать
        PathBuilder<Item> path = new PathBuilder<>(Item.class, qItem.getMetadata());
        ComparableExpressionBase<Long> fieldExpression = path.getNumber(fieldName, Long.class);
        query.orderBy(fieldExpression.asc());


//        Long last = 10L;
//        ComparableExpressionBase<Long> fieldExpression2 = path.getNumber(fieldName, Long.class);
//        BooleanBuilder builder2= new BooleanBuilder();
//        builder2.and(Expressions.asBoolean(fieldExpression2.lt(last)));

        Long last = 10L;
        ComparableExpressionBase<Long> fieldExpression2 = path.getNumber(fieldName, Long.class);
        BooleanBuilder builder2 = new BooleanBuilder();
        builder2.and(fieldExpression2.lt(last).isTrue());
 */
