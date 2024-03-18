package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validate.Validate;

import java.time.LocalDateTime;
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

    private boolean isFilter(Item item, String text) {
        if (text.isBlank()) {
            return false;
        }
        return item.getAvailable()
                && (item.getName().toLowerCase().contains(text.toLowerCase())
                || item.getDescription().toLowerCase().contains(text.toLowerCase()));
    }

    @Override
    public Item create(Long userId, Item item, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        userService.getByIdUser(userId);
        item.setOwner(userId);
        log.info("Вещь успешно добавлена. {}", item);
        return repository.save(item);
    }

    @Override
    public Item update(Long itemId, Long userId, Item item) {
        Item itemLast = getByIdItem(itemId, userId);
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
        return repository.save(item);
    }

    @Override
    public List<ItemWithBookingDto> getItems(Long userId) {
        return repository.findAll().stream()
                .filter(item -> item.getOwner().equals(userId))
                .map(item -> ItemMapper.toItemWithBookingDto(item,
                        bookingRepository.getLastBooking(userId, item.getId(), LocalDateTime.now()),
                        bookingRepository.getNextBooking(userId, LocalDateTime.now(), item.getId()),
                        commentRepository.findByItemId(item.getId()).stream()
                                .map(ItemMapper::toCommentDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public Item getByIdItem(Long itemId, Long userId) {
        userService.getByIdUser(userId);
        Optional<Item> item = repository.findById(itemId);
        if (item.isEmpty()) {
            throw new FailIdException("Неверный id!");
        }
        return item.get();
    }

    @Override
    public List<Item> getItemsBySearch(String text) {
        return repository.findAll().stream().filter(item -> isFilter(item, text)).collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Comment comment, Long userId, Long itemId, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        User user = userService.getByIdUser(userId);
        if (bookingRepository.findByBookerAndStatusAndEndBefore(userId, Status.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("Нельзя оставлять комментарии!");
        }
        comment.setItemId(itemId);
        comment.setAuthor(user);
        log.info("Комментарий успешно добавлен. {}", comment);
        return ItemMapper.toCommentDto(commentRepository.save(comment));
    }
}
