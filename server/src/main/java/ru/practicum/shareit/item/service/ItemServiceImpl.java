package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.exceptions.ValidationExceptionRun;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    private final UserService userService;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        userService.getByIdUser(userId);
        item.setOwner(userId);
        log.info("Вещь успешно добавлена. {}", item);
        return ItemMapper.toItemDto(repository.save(item));
    }

    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        Item itemLast = ItemMapper.toItem(getByIdItem(itemId, userId));
        if (!Objects.equals(itemLast.getOwner(), userId)) {
            throw new FailIdException("Неверный id!");
        }
        if (item.getName() == null) {
            item.setName(itemLast.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(itemLast.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(itemLast.getAvailable());
        }
        item.setId(itemId);
        item.setOwner(userId);
        log.info("Вещь успешно обновлена. {}", item);
        return ItemMapper.toItemDto(repository.save(item));
    }

    @Override
    public List<ItemWithBookingDto> getItems(Long userId, int from, int size) {
        userService.getByIdUser(userId);
        return repository.findByOwner(userId,
                        PageRequest.of(from / size, size, Sort.by("id")))
                .stream()
                .map(item -> ItemMapper.toItemWithBookingDto(item,
                        bookingRepository.getLastBooking(userId, item.getId(), LocalDateTime.now()),
                        bookingRepository.getNextBooking(userId, LocalDateTime.now(), item.getId()),
                        commentRepository.findByItemId(item.getId()).stream()
                                .map(ItemMapper::toCommentDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemWithBookingDto getByIdItem(Long itemId, Long userId) {
        userService.getByIdUser(userId);
        Optional<Item> item = repository.findById(itemId);
        if (item.isEmpty()) {
            throw new FailIdException("Неверный id!");
        }
        return ItemMapper.toItemWithBookingDto(item.get(),
                bookingRepository.getLastBooking(userId, itemId, LocalDateTime.now()),
                bookingRepository.getNextBooking(userId, LocalDateTime.now(), itemId),
                commentRepository.findByItemId(itemId).stream()
                        .map(ItemMapper::toCommentDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text, Long userId, int from, int size) {
        userService.getByIdUser(userId);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return repository.findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue(text, text,
                        PageRequest.of(from / size, size))
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long userId, Long itemId) {
        Comment comment = ItemMapper.toComment(commentDto);
        User user = UserMapper.toUser(userService.getByIdUser(userId));
        if (bookingRepository.findByBookerAndStatusAndEndBefore(userId, Status.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new ValidationExceptionRun("Нельзя оставлять комментарии!");
        }
        comment.setItemId(itemId);
        comment.setAuthor(user);
        log.info("Комментарий успешно добавлен. {}", comment);
        return ItemMapper.toCommentDto(commentRepository.save(comment));
    }
}
