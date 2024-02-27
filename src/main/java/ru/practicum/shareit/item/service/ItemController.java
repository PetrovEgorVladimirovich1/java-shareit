package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
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
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto getByIdItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemMapper.toItemDto(itemService.getByIdItem(itemId, userId));
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam(name = "text", defaultValue = "", required = false) String text) {
        return itemService.getItemsBySearch(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
