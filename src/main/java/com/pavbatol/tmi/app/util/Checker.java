package com.pavbatol.tmi.app.util;

import com.pavbatol.tmi.app.exception.NotFoundException;
import com.pavbatol.tmi.app.exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Checker {
    public static final String S_WITH_ID_S_WAS_NOT_FOUND = "%s with id=%s was not found";

    public static void checkPaginationArguments(@NotNull String idFieldName,
                                                String sortFieldName,
                                                Long lastIdValue,
                                                String lastSortFieldValue) {
        if (!(lastIdValue == null && lastSortFieldValue == null)
                && !(lastIdValue != null && lastSortFieldValue != null && sortFieldName != null)) {
            throw new ValidationException("Specify arguments 'lastIdValue', 'lastSortFieldValue', 'sortFieldName'");
        }
        if (idFieldName.equals(sortFieldName) && lastIdValue != null
                && !String.valueOf(lastIdValue).equals(lastSortFieldValue)) {
            throw new ValidationException("When sorting by id, the values must match for 'lastIdValue', 'lastSortFieldValue'");
        }
    }

    @NotNull
    public static <T, I> T getNonNullObject(@NotNull CrudRepository<T, I> storage, I id) throws NotFoundException {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(S_WITH_ID_S_WAS_NOT_FOUND,
                        getTClass(storage).getSimpleName(), id)));
    }

    public static <T, I> void checkId(@NotNull CrudRepository<T, I> storage, I id) throws NotFoundException {
        if (!storage.existsById(id)) {
            throw new NotFoundException(String.format(S_WITH_ID_S_WAS_NOT_FOUND,
                    getTClass(storage).getSimpleName(), id));
        }
    }

    private static <T, I> Class<T> getTClass(CrudRepository<T, I> storage) {
        ResolvableType resolvableType = ResolvableType.forClass(storage.getClass()).as(CrudRepository.class);
        return (Class<T>) resolvableType.getGeneric(0).toClass();
    }
}