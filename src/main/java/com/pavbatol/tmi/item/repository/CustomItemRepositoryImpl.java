package com.pavbatol.tmi.item.repository;

import com.pavbatol.tmi.app.exception.ValidationException;
import com.pavbatol.tmi.item.model.Item;
import com.pavbatol.tmi.item.model.enums.ItemSort;
import com.pavbatol.tmi.item.model.enums.ItemType;
import com.pavbatol.tmi.item.model.QItem;
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
public class CustomItemRepositoryImpl implements CustomItemRepository {
    public static final String ID = ItemSort.ID.getFieldName();
    private final EntityManager entityManager;
    private boolean onlySort;

    @Override
    public List<Item> findAllByPagination(Long lastIdValue,
                                          String lastSortFieldValue,
                                          String sortFieldName,
                                          Sort.Direction direction,
                                          Integer limit) {
        QItem qItem = QItem.item;
        BooleanBuilder builder = new BooleanBuilder();
        JPAQuery<Item> query = new JPAQuery<>(entityManager);

        checkAndCorrectArguments(lastIdValue, lastSortFieldValue, sortFieldName);
        if (lastIdValue == null || lastSortFieldValue == null) {
            onlySort = true;
        }

        EnumPath<ItemType> enumPath = Expressions.enumPath(ItemType.class, qItem, sortFieldName);

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
                if (direction == Sort.Direction.DESC) {
                    builder.and(qItem.id.lt(lastIdValue));
                } else {
                    builder.and(qItem.id.gt(lastIdValue));
                }
            } else {
                if (direction == Sort.Direction.DESC) {
                    builder.and(enumPath.stringValue().lt(lastSortFieldValue)
                            .or(enumPath.stringValue().eq(lastSortFieldValue).and(qItem.id.lt(lastIdValue))));
                } else {
                    builder.and(enumPath.stringValue().gt(lastSortFieldValue)
                            .or(enumPath.stringValue().eq(lastSortFieldValue).and(qItem.id.gt(lastIdValue))));
                }
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
