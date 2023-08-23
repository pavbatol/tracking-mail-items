package com.pavbatol.tmi.item.service;

import com.pavbatol.tmi.app.exception.ValidationException;
import com.pavbatol.tmi.item.dto.ItemDto;
import com.pavbatol.tmi.item.dto.ItemDtoAddRequest;
import com.pavbatol.tmi.item.dto.ItemDtoUpdate;
import com.pavbatol.tmi.item.mapper.ItemMapper;
import com.pavbatol.tmi.item.model.Item;
import com.pavbatol.tmi.item.model.enums.ItemSort;
import com.pavbatol.tmi.item.model.enums.ItemType;
import com.pavbatol.tmi.item.repository.ItemJpaRepository;
import com.pavbatol.tmi.operation.dto.OperationDto;
import com.pavbatol.tmi.operation.dto.OperationDtoAddRequest;
import com.pavbatol.tmi.operation.model.enums.OperationType;
import com.pavbatol.tmi.operation.service.OperationService;
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
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class,})
class ItemServiceImplTest {
    private static final int POST_CODE = 100;
    private static final String POST_NAME = "name";
    private static final String POST_ADDRESS = "address";
    public static final String RECEIVER_NAME = "receiverName";
    public static final String RECEIVER_ADDRESS = "receiverAddress";

    @Mock
    private ItemJpaRepository itemRepository;
    private final ItemMapper mapper = Mappers.getMapper(ItemMapper.class);
    private ItemServiceImpl itemService;
    @Mock
    private OperationService operationService;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    private Post post;
    private PostDto postDto;
    private Item item;
    private ItemDto itemDto;
    ItemType type = ItemType.LETTER;


    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, mapper, operationService);
        post = new Post()
                .setPostCode(POST_CODE)
                .setPostName(POST_NAME)
                .setPostAddress(POST_ADDRESS);
        postDto = new PostDto(POST_CODE, POST_NAME, POST_ADDRESS);
        item = new Item()
                .setId(1L)
                .setPost(post)
                .setType(type)
                .setReceiverAddress(RECEIVER_ADDRESS)
                .setReceiverName(RECEIVER_NAME);
        itemDto = new ItemDto(
                1L,
                type,
                postDto,
                RECEIVER_ADDRESS,
                RECEIVER_NAME
        );
    }

    @Test
    void add_shouldCorrectMapping_andRevokeOperationServiceAndItemRepository() {
        Integer postCode = POST_CODE;
        Item savedItem = item;
        ItemDtoAddRequest dtoAddRequest = new ItemDtoAddRequest(type, postDto, RECEIVER_ADDRESS, RECEIVER_NAME);
        OperationDtoAddRequest operationDtoAddRequest = new OperationDtoAddRequest(
                itemDto,
                new PostDto(postCode, null, null),
                OperationType.REGISTER
        );
        OperationDto operationDto = new OperationDto(
                1L,
                itemDto,
                postDto,
                OperationType.REGISTER,
                LocalDateTime.now()
        );

        when(itemRepository.save(any())).thenReturn(savedItem);
        when(operationService.add(any())).thenReturn(operationDto);

        itemService.add(postCode, dtoAddRequest);

        verify(itemRepository, times(1)).save(itemArgumentCaptor.capture());

        Item valueItem = itemArgumentCaptor.getValue();

        assertThat(valueItem.getId()).isEqualTo(null);
        assertThat(valueItem.getPost()).isEqualTo(item.getPost());
        assertThat(valueItem.getType()).isEqualTo(item.getType());
        assertThat(valueItem.getReceiverName()).isEqualTo(item.getReceiverName());
        assertThat(valueItem.getReceiverAddress()).isEqualTo(item.getReceiverAddress());

        verify(operationService, times(1)).add(argThat(request1 ->
                request1.getType().equals(operationDtoAddRequest.getType())
                        && request1.getPost().equals(operationDtoAddRequest.getPost())
                        && request1.getItem().equals(operationDtoAddRequest.getItem())
        ));
    }

    @Test
    void update() {
        String newName = RECEIVER_NAME + "updated";
        ItemDtoUpdate dtoUpdate = new ItemDtoUpdate(type, postDto, RECEIVER_ADDRESS, newName);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenAnswer(invocationOnMock -> {
            Item item1 = invocationOnMock.getArgument(0, Item.class);
            item1.setId(item1.getId());
            return item1;
        });

        ItemDto updated = itemService.update(item.getId(), dtoUpdate);

        assertThat(updated).isNotNull();
        verify(itemRepository, times(1)).save(itemArgumentCaptor.capture());

        Item captorValue = itemArgumentCaptor.getValue();

        assertThat(captorValue.getReceiverName()).isEqualTo(newName);
        assertThat(captorValue.getReceiverAddress()).isEqualTo(item.getReceiverAddress());
    }

    @Test
    void remove_shouldRevokeRepo() {
        long itemId = 1L;
        itemService.remove(itemId);

        verify(itemRepository, times(1)).deleteById(itemId);
    }

    @Test
    void findById_shouldRevokeRepo() {
        long itemId = 1L;

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        itemService.findById(itemId);

        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void findAll_shouldSetItemSort_whenItIsNull() {
        Long lastIdValue = null;
        String lastSortFieldValue = null;
        ItemSort itemSort = null;
        Sort.Direction direction = Sort.Direction.DESC;
        Integer limit = 15;

        ItemSort ExpectedItemSortFieldName = ItemSort.TYPE;

        when(itemRepository.findAllByPagination(eq(lastIdValue), eq(lastSortFieldValue), eq(ExpectedItemSortFieldName.getFieldName()), eq(direction), eq(limit)))
                .thenReturn(List.of(item));

        List<ItemDto> dtos = itemService.findAll(
                lastIdValue,
                lastSortFieldValue,
                itemSort,
                direction,
                limit);

        verify(itemRepository, times(1)).findAllByPagination(
                lastIdValue,
                lastSortFieldValue,
                ExpectedItemSortFieldName.getFieldName(),
                direction,
                limit);
    }

    @Test
    void findAll_shouldThrowValidationException_whenMissingItemSort() {
        Long lastIdValue = 1L;
        String lastSortFieldValue = ItemType.LETTER.name();
        ItemSort itemSort = null;
        Sort.Direction direction = Sort.Direction.DESC;
        Integer limit = 15;

        assertThatThrownBy(() -> {
            itemService.findAll(lastIdValue, lastSortFieldValue, itemSort, direction, limit);
        })
                .isInstanceOf(ValidationException.class)
                .hasMessage("Missing 'itemSort' when specified 'lastIdValue' or 'lastSortFieldValue' argument");
    }
}