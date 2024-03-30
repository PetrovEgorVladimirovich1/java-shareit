package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.item.service.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    private ItemRequestService itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userService, itemRepository);
        Mockito.when(userService.getByIdUser(Mockito.anyLong())).thenReturn(new UserDto());
    }

    @Test
    void create() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description", 1L,
                LocalDateTime.now());
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(ItemRequestMapper.toItemRequest(itemRequestDto));
        assertEquals(itemRequestDto, itemRequestService.create(1L, itemRequestDto));

    }

    @Test
    void getItemRequests() {
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(new ItemRequest());
        Mockito.when(itemRequestRepository.findByRequestorOrderByCreatedDesc(Mockito.anyLong()))
                .thenReturn(itemRequests);
        assertEquals(1, itemRequestService.getItemRequests(1L).size());
    }

    @Test
    void getItemRequestsOtherUsers() {
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(new ItemRequest());
        Mockito.when(itemRequestRepository.findByRequestorNot(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .thenReturn(itemRequests);
        assertEquals(1, itemRequestService.getItemRequestsOtherUsers(1L, 1, 1).size());
    }

    @Test
    void getByIdItemRequest() {
        ItemRequest item = new ItemRequest(1L, "description", 1L, LocalDateTime.now());
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito.when(itemRequestRepository.findByIdAndRequestor(Mockito.anyLong(), Mockito.anyLong())).thenReturn(item);
        Mockito.when(itemRepository.findByRequestId(Mockito.anyLong())).thenReturn(new ArrayList<>());
        assertEquals(ItemRequestMapper.toItemRequestWithItemDto(item, new ArrayList<>()),
                itemRequestService.getByIdItemRequest(1L, 1L));
        assertEquals(ItemRequestMapper.toItemRequestWithItemDto(item, new ArrayList<>()),
                itemRequestService.getByIdItemRequest(1L, 2L));
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(FailIdException.class, () -> itemRequestService.getByIdItemRequest(1L, 1L));
    }
}