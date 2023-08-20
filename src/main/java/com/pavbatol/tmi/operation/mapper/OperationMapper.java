package com.pavbatol.tmi.operation.mapper;

import com.pavbatol.tmi.operation.dto.OperationDto;
import com.pavbatol.tmi.operation.dto.OperationDtoAddRequest;
import com.pavbatol.tmi.operation.model.Operation;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OperationMapper {
    Operation toEntity(OperationDtoAddRequest dto);

    OperationDto toDto(Operation operation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Operation partialUpdate(OperationDto dto, @MappingTarget Operation operation);

    List<OperationDto> toDtos(List<Operation> operations);
}