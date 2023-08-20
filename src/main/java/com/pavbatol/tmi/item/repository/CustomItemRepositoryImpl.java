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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomItemRepositoryImpl implements CustomItemRepository {
    public static final String ID = "id";
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

//    /**
//     * Allowed<br>
//     * lastIdValue == null, lastSortFieldValue == null, sortFieldName != null<br>
//     * lastIdValue != null, lastSortFieldValue != null, sortFieldName != null<br>
//     * lastIdValue != null, lastSortFieldValue == null, sortFieldName == ID<br>
//     * lastIdValue != null, lastSortFieldValue == null, sortFieldName == null<br>
//     * lastIdValue == null, lastSortFieldValue == null, sortFieldName == null<br>
//     */
//    private void checkAndCorrectArguments(Long lastIdValue, String lastSortFieldValue, String sortFieldName) {
//        String areCorrect = "Input arguments are correct";
//        if ((lastIdValue == null && lastSortFieldValue == null && sortFieldName != null)
//                || (lastIdValue != null && lastSortFieldValue != null && sortFieldName != null)) {
//            log.debug(areCorrect);
//        } else if (lastIdValue != null && lastSortFieldValue == null && ID.equals(sortFieldName)) {
//            log.debug(areCorrect);
//            lastSortFieldValue = String.valueOf(lastIdValue);
//        } else if (lastIdValue != null && lastSortFieldValue == null && sortFieldName == null) {
//            log.debug(areCorrect);
//            lastSortFieldValue = String.valueOf(lastIdValue);
//            sortFieldName = ID;
//        } else if (lastIdValue == null && lastSortFieldValue == null && sortFieldName == null) {
//            log.debug(areCorrect);
//            sortFieldName = ID;
//        } else {
//            throw new ValidationException("Specify arguments 'lastIdValue', 'lastSortFieldValue', 'sortFieldName'");
//        }
//
////        if (lastIdValue != null && sortFieldName == null) {
////            lastSortFieldValue = lastSortFieldValue != null ? lastSortFieldValue : String.valueOf(lastIdValue);
////            if (String.valueOf(lastIdValue).equals(lastSortFieldValue)) {
////                sortFieldName = ID;
////            }
////        }
////        if (sortFieldName != null && lastIdValue != null && lastSortFieldValue != null) {
////            if (ID.equals(sortFieldName) && !String.valueOf(lastIdValue).equals(lastSortFieldValue)) {
////                throw new ValidationException("The sorting field denoted by " + ID + ", the values of 'lastIdValue' and 'lastSortFieldValue' diverge");
////            }
////        }
////        if (lastSortFieldValue != null && (lastIdValue == null || sortFieldName == null)) {
////            throw new ValidationException("Missing 'lastIdValue' or 'sortFieldName' argument when specifying 'lastSortFieldValue'");
////        }
//    }

    private void checkAndCorrectArguments(Long lastIdValue, String lastSortFieldValue, String sortFieldName) {
        if (!(lastIdValue == null && lastSortFieldValue == null)
                && !(lastIdValue != null && lastSortFieldValue != null && sortFieldName != null)) {
            throw new ValidationException("Specify arguments 'lastIdValue', 'lastSortFieldValue', 'sortFieldName'");
        }
        if (sortFieldName == null) {
            sortFieldName = ID;
        }
        if (lastIdValue == null || lastSortFieldValue == null) {
            onlySort = true;
        }
    }
}
