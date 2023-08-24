package com.pavbatol.tmi.operation.service;

import com.pavbatol.tmi.app.exception.ValidationException;
import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.item.model.Item;
import com.pavbatol.tmi.item.model.enums.ItemType;
import com.pavbatol.tmi.operation.dto.OperationDto;
import com.pavbatol.tmi.operation.dto.OperationDtoAddRequest;
import com.pavbatol.tmi.operation.dto.OperationDtoUpdate;
import com.pavbatol.tmi.operation.mapper.OperationMapper;
import com.pavbatol.tmi.operation.model.Operation;
import com.pavbatol.tmi.operation.model.enums.OperationSort;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.pavbatol.tmi.operation.model.filter.OperationFilter;
import com.pavbatol.tmi.operation.repository.OperationJpaRepository;
import com.pavbatol.tmi.post.dto.PostDto;
import com.pavbatol.tmi.post.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class,})
class OperationServiceImplTest {
    private static final int POST_CODE = 100;
    private static final String POST_NAME = "name";
    private static final String POST_ADDRESS = "address";
    public static final String RECEIVER_NAME = "receiverName";
    public static final String RECEIVER_ADDRESS = "receiverAddress";
    public static final long ID = 1L;

    @Mock
    private OperationJpaRepository repository;
    private OperationServiceImpl service;
    private final OperationMapper mapper = Mappers.getMapper(OperationMapper.class);

    private Operation operation;
    private OperationDto operationDto;
    private OperationType operationType = OperationType.REGISTER;
    private PostDto postDto;
    private Item item;
    private Post post;
    private ItemDto itemDto;
    ItemType itemType = ItemType.LETTER;
    LocalDateTime currentDateTime = LocalDateTime.now().minusMinutes(1);

    @Captor
    private ArgumentCaptor<Operation> operationArgumentCaptor;

    @BeforeEach
    void setUp() {
        service = new OperationServiceImpl(repository, mapper);
        postDto = new PostDto(POST_CODE, POST_NAME, POST_ADDRESS);
        itemDto = new ItemDto(ID, itemType, postDto, RECEIVER_ADDRESS, RECEIVER_NAME);
        post = new Post().setPostCode(POST_CODE).setPostName(POST_NAME).setPostAddress(POST_ADDRESS);
        item = new Item().setId(ID).setPost(post).setType(itemType).setReceiverAddress(RECEIVER_ADDRESS).setReceiverName(RECEIVER_NAME);
        operation = new Operation()
                .setId(ID)
                .setItem(item)
                .setPost(post)
                .setType(operationType)
                .setOperatedOn(currentDateTime);
        operationDto = new OperationDto(ID,
                itemDto,
                postDto,
                operationType,
                currentDateTime);
    }

    @Test
    void add_shouldSetDateTimeAndInvokeRopo() {
        OperationDtoAddRequest dtoAddRequest = new OperationDtoAddRequest(itemDto, postDto, OperationType.REGISTER);

        when(repository.save(any())).thenReturn(operation);

        service.add(dtoAddRequest);

        verify(repository, times(1)).save(argThat(operation1 ->
                operation1.getType().equals(operationType)
                        && operation1.getOperatedOn().isAfter(currentDateTime)
                        && operation1.getPost().getPostCode().equals(post.getPostCode())
                        && operation1.getItem().getId().equals(item.getId())
        ));
    }

    @Test
    void update_shouldCheckItemIdAndOnlyOneFieldIsUpdated() {
        OperationType mewType = OperationType.DEPART;
        OperationDtoUpdate dtoUpdate = new OperationDtoUpdate(itemDto, postDto, mewType);

        when(repository.findById(anyLong())).thenReturn(Optional.of(operation));

        when(repository.save(any())).thenAnswer(invocationOnMock -> {
            Operation operation1 = invocationOnMock.getArgument(0, Operation.class);
            operation1.setId(operation.getId());
            return operation1;
        });

        OperationDto updated = service.update(operation.getId(), dtoUpdate);

        assertThat(updated).isNotNull();
        verify(repository, times(1)).save(operationArgumentCaptor.capture());

        Operation captorValue = operationArgumentCaptor.getValue();

        assertThat(captorValue.getType()).isEqualTo(mewType);
        assertThat(captorValue.getOperatedOn()).isEqualTo(operation.getOperatedOn());
    }

    @Test
    void remove_shouldInvokeRepo() {
        service.remove(ID);

        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    void findById_shouldInvokeRepo() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(operation));

        service.findById(ID);

        verify(repository, times(1)).findById(ID);
    }

    @Test
    void findAll_shouldSeOperationSort_whenItIsNull() {
        OperationFilter filter = new OperationFilter(null, null, null, null, null);
        Long lastIdValue = null;
        String lastSortFieldValue = null;
        OperationSort operationSort = null;
        Sort.Direction direction = Sort.Direction.DESC;
        Integer limit = 15;

        OperationSort ExpectedOperationSort = OperationSort.TYPE;
        when(repository.findAllByPagination(filter, lastIdValue, lastSortFieldValue, ExpectedOperationSort.getFieldName(), direction, limit))
                .thenReturn(List.of(operation));


        service.findAll(
                filter,
                lastIdValue,
                lastSortFieldValue,
                ExpectedOperationSort,
                direction,
                limit);

        verify(repository, times(1)).findAllByPagination(
                filter,
                lastIdValue,
                lastSortFieldValue,
                ExpectedOperationSort.getFieldName(),
                direction,
                limit);
    }

    @Test
    void findAll_shouldThrowValidationException_whenMissingOperationSortAndSpecifiedLastIdValueOrLastSortFieldValue() {
        OperationFilter filter = new OperationFilter(null, null, null, null, null);
        Long lastIdValue = 1L;
        String lastSortFieldValue = "1Э";
        OperationSort operationSort = null;
        Sort.Direction direction = Sort.Direction.DESC;
        Integer limit = 15;

        assertThatThrownBy(() -> {
            service.findAll(filter, lastIdValue, lastSortFieldValue, operationSort, direction, limit);
        })
                .isInstanceOf(ValidationException.class)
                .hasMessage("Missing 'operationSort' when specified 'lastIdValue' or 'lastSortFieldValue' argument");
    }

    @Test
    void getItemTrack_shouldSetStartAndEnd_whenTheyAreNull() {
        Long itemId = ID;
        LocalDateTime start = null;
        LocalDateTime end = null;
        Sort.Direction direction = Sort.Direction.DESC;

        LocalDateTime startExpected = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        LocalDateTime endMinimumExpected = LocalDateTime.now().minusMinutes(1);
        PageRequest pageRequestExpected = PageRequest.of(
                0, Integer.MAX_VALUE, Sort.by(direction, OperationSort.TIMESTAMP.getFieldName()));
        ArgumentCaptor<LocalDateTime> localDateTimeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        when(repository.findAllByItemIdAndOperatedOnBetween(eq(itemId), eq(startExpected), any(LocalDateTime.class), eq(pageRequestExpected)))
                .thenReturn(List.of(operation));

        service.getItemTrack(itemId, start, end, direction);

        verify(repository, times(1))
                .findAllByItemIdAndOperatedOnBetween(eq(itemId), eq(startExpected), localDateTimeCaptor.capture(), eq(pageRequestExpected));

        LocalDateTime endTimeCaptorValue = localDateTimeCaptor.getValue();

        assertThat(endTimeCaptorValue).isAfter(endMinimumExpected);
    }
}