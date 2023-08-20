package com.pavbatol.tmi.operation.model.filter;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class OperationFilter {
    List<Long> ItemIds;

    List<Long> PostCodes;

    String type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime rangeStartOperatedOn;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime rangeEndOperatedOn;
}
