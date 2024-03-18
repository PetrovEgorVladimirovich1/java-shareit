package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.service.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @PostMapping
    public ItemDto create(@Valid @RequestBody Item item,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          BindingResult bindingResult) {
        return ItemMapper.toItemDto(itemService.create(userId, item, bindingResult));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody Item item) {
        return ItemMapper.toItemDto(itemService.update(itemId, userId, item));
    }

    @GetMapping
    public List<ItemWithBookingDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingDto getByIdItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemMapper.toItemWithBookingDto(itemService.getByIdItem(itemId, userId),
                bookingRepository.getLastBooking(userId, itemId, LocalDateTime.now()),
                bookingRepository.getNextBooking(userId, LocalDateTime.now(), itemId),
                commentRepository.findByItemId(itemId).stream()
                        .map(ItemMapper::toCommentDto)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam(name = "text", defaultValue = "", required = false) String text) {
        return itemService.getItemsBySearch(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody Comment comment,
                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId,
                                    BindingResult bindingResult) {
        return itemService.createComment(comment, userId, itemId, bindingResult);
    }
}
