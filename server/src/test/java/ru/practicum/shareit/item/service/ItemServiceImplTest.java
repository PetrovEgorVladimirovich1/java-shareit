package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.exceptions.ValidationExceptionRun;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userService, bookingRepository, commentRepository);
    }

    @Test
    void create() {
        Item item = new Item(1L, "name", "description", true, 1L, 1L);
        Mockito.when(userService.getByIdUser(Mockito.anyLong())).thenReturn(new UserDto());
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);
        assertEquals(ItemMapper.toItemDto(item), itemService.create(1L, ItemMapper.toItemDto(item)));
    }

    @Test
    void update() {
        Item item = new Item(1L, "name", "description", true, 1L, 1L);
        Mockito.when(userService.getByIdUser(Mockito.anyLong())).thenReturn(new UserDto());
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);
        ItemDto itemDto = new ItemDto(1L, "name", "description", true, 1L);
        assertEquals(itemDto, itemService.update(1L, 1L, ItemMapper.toItemDto(item)));
        Mockito.verify(itemRepository, Mockito.times(1)).save(item);
        assertThrows(FailIdException.class, () -> itemService.update(1L, 2L, ItemMapper.toItemDto(item)));
        itemDto.setName(null);
        itemDto.setDescription(null);
        itemDto.setAvailable(null);
        assertEquals(ItemMapper.toItemDto(item), itemService.update(1L, 1L, itemDto));
    }

    @Test
    void getItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "name", "description", true, 1L, 1L));
        Mockito.when(userService.getByIdUser(Mockito.anyLong())).thenReturn(new UserDto());
        Mockito.when(itemRepository.findByOwner(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(items);
        assertEquals(1, itemService.getItems(1L, 1, 1).size());
    }

    @Test
    void getByIdItem() {
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(FailIdException.class, () -> itemService.getByIdItem(1L, 1L));
    }

    @Test
    void getItemsBySearch() {
        Mockito.when(userService.getByIdUser(Mockito.anyLong())).thenReturn(new UserDto());
        Mockito.when(itemRepository.findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue(
                        Mockito.any(String.class), Mockito.any(String.class), Mockito.any(PageRequest.class)))
                .thenReturn(new ArrayList<>());
        assertTrue(itemService.getItemsBySearch("name", 1L, 1, 1).isEmpty());
        assertTrue(itemService.getItemsBySearch("   ", 1L, 1, 1).isEmpty());
    }

    @Test
    void createComment() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        CommentDto commentDto = new CommentDto(1L, "text", "name", LocalDateTime.now());
        UserDto userDto = new UserDto(1L, "name", "egich-2011@mail.ru");
        Comment comment = new Comment(1L, "text", 1L, UserMapper.toUser(userDto));
        Mockito.when(userService.getByIdUser(Mockito.anyLong())).thenReturn(userDto);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        assertThrows(ValidationExceptionRun.class, () -> itemService.createComment(ItemMapper
                .toCommentDto(comment), 1L, 1L));
        Mockito.when(bookingRepository.findByBookerAndStatusAndEndBefore(Mockito.anyLong(), Mockito.any(),
                Mockito.any(LocalDateTime.class))).thenReturn(bookings);
        CommentDto commentDto1 = itemService.createComment(ItemMapper.toCommentDto(comment), 1L, 1L);
        commentDto1.setCreated(commentDto.getCreated());
        assertEquals(commentDto, commentDto1);
    }
}