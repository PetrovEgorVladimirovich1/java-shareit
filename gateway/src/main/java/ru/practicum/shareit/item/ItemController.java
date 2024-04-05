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
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@PathVariable Long itemId,
                                         @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemClientDto itemDto) {
        return itemClient.update(itemId, userId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(name = "from", defaultValue = "0")
                                           @PositiveOrZero int from,
                                           @RequestParam(name = "size", defaultValue = "10")
                                           @Positive int size) {
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getByIdItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getByIdItem(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearch(@RequestParam(name = "text", defaultValue = "", required = false) String text,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(name = "from", defaultValue = "0")
                                                   @PositiveOrZero int from,
                                                   @RequestParam(name = "size", defaultValue = "10")
                                                   @Positive int size) {
        return itemClient.getItemsBySearch(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentClientDto commentDto,
                                                @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId) {
        return itemClient.createComment(commentDto, userId, itemId);
    }
}
