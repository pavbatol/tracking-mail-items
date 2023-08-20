package com.pavbatol.tmi.item.repository;

import com.pavbatol.tmi.app.exception.ValidationException;
import com.pavbatol.tmi.item.model.Item;
import com.pavbatol.tmi.item.model.ItemType;
import com.pavbatol.tmi.item.model.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomItemRepositoryImpl implements CustomItemRepository {
    public static final String ID = "id";
    private final EntityManager entityManager;

    @Override
    public List<Item> findAllByPagination_3(Long lastIdValue,
                                            String lastSortFieldValue,
                                            String sortFieldName,
                                            Sort.Direction direction,
                                            Integer limit) {
        QItem qItem = QItem.item;
        BooleanBuilder builder = new BooleanBuilder();
        JPAQuery<Item> query = new JPAQuery<>(entityManager);

        correctAndCheckArguments(lastIdValue, lastSortFieldValue, sortFieldName);

        EnumPath<ItemType> enumPath = null;
        if (sortFieldName != null) {
            enumPath = Expressions.enumPath(ItemType.class, qItem, sortFieldName);
        }

        if (sortFieldName == null || ID.equals(sortFieldName)) {
            query = direction == Sort.Direction.DESC
                    ? query.orderBy(qItem.id.desc())
                    : query.orderBy(qItem.id.asc());
        } else {
            query = direction == Sort.Direction.DESC
                    ? query.orderBy(enumPath.desc(), qItem.id.desc())
                    : query.orderBy(enumPath.asc(), qItem.id.asc());
        }

        if (enumPath != null && lastSortFieldValue != null && lastIdValue != null) {
            if (direction == Sort.Direction.DESC) {
                builder.and(enumPath.stringValue().lt(lastSortFieldValue)
                        .or(enumPath.stringValue().eq(lastSortFieldValue).and(qItem.id.lt(lastIdValue))));
            } else {
                builder.and(enumPath.stringValue().gt(lastSortFieldValue)
                        .or(enumPath.stringValue().eq(lastSortFieldValue).and(qItem.id.gt(lastIdValue))));
            }
        } else if (lastIdValue != null) {
            if (direction == Sort.Direction.DESC) {
                builder.and(qItem.id.lt(lastIdValue));
            } else {
                builder.and(qItem.id.gt(lastIdValue));
            }
        }

        if (limit != null) {
            query.limit(limit);
        }

        return query.from(qItem).where(builder).fetch();
    }

    // TODO: 19.08.2023 Consider lastIdValue !=null, lastSortFieldValue == nul, sortFieldName != null
    private void correctAndCheckArguments(Long lastIdValue, String lastSortFieldValue, String sortFieldName) {
        if (lastIdValue != null && sortFieldName == null) {
            lastSortFieldValue = lastSortFieldValue != null ? lastSortFieldValue : String.valueOf(lastIdValue);
            if (String.valueOf(lastIdValue).equals(lastSortFieldValue)) {
                sortFieldName = ID;
            }
        }
        if (sortFieldName != null && lastIdValue != null && lastSortFieldValue != null) {
            if (ID.equals(sortFieldName) && !String.valueOf(lastIdValue).equals(lastSortFieldValue)) {
                throw new ValidationException("The sorting field denoted by " + ID + ", the values of 'lastIdValue' and 'lastSortFieldValue' diverge");
            }
        }
        if (lastSortFieldValue != null && (lastIdValue == null || sortFieldName == null)) {
            throw new ValidationException("Missing 'lastIdValue' or 'sortFieldName' argument when specifying 'lastSortFieldValue'");
        }
    }
}
