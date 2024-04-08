package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentClientDto;
import ru.practicum.shareit.item.dto.ItemClientDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemClientDto itemDto,
                                         @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Positive @PathVariable Long itemId,
                                         @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemClientDto itemDto) {
        return itemClient.update(itemId, userId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(name = "from", defaultValue = "0")
                                           @PositiveOrZero int from,
                                           @RequestParam(name = "size", defaultValue = "10")
                                           @Positive int size) {
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getByIdItem(@Positive @PathVariable Long itemId,
                                              @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getByIdItem(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearch(@RequestParam(name = "text", defaultValue = "", required = false) String text,
                                                   @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(name = "from", defaultValue = "0")
                                                   @PositiveOrZero int from,
                                                   @RequestParam(name = "size", defaultValue = "10")
                                                   @Positive int size) {
        return itemClient.getItemsBySearch(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentClientDto commentDto,
                                                @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Positive @PathVariable Long itemId) {
        return itemClient.createComment(commentDto, userId, itemId);
    }
}
