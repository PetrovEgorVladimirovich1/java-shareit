package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody ItemDto itemDto) {
        return itemService.update(itemId, userId, itemDto);
    }

    @GetMapping
    public List<ItemWithBookingDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(name = "from", defaultValue = "0")
                                             @PositiveOrZero int from,
                                             @RequestParam(name = "size", defaultValue = "10")
                                             @Positive int size) {
        return itemService.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingDto getByIdItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getByIdItem(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam(name = "text", defaultValue = "", required = false) String text,
                                          @RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(name = "from", defaultValue = "0")
                                          @PositiveOrZero int from,
                                          @RequestParam(name = "size", defaultValue = "10")
                                          @Positive int size) {
        return itemService.getItemsBySearch(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId) {
        return itemService.createComment(commentDto, userId, itemId);
    }
}
