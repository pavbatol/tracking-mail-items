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

import static com.pavbatol.tmi.app.util.Checker.checkPaginationArguments;

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

        checkPaginationArguments(ID, sortFieldName, lastIdValue, lastSortFieldValue);
        if (sortFieldName == null) {
            sortFieldName = ID;
        }
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

        if (limit != null) {
            query.limit(limit);
        }

        return query.from(qItem).where(builder).fetch();
    }
}
