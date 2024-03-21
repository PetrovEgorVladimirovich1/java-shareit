package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long itemId, Long userId, ItemDto itemDto);

    List<ItemWithBookingDto> getItems(Long userId);

    ItemWithBookingDto getByIdItem(Long itemId, Long userId);

    List<ItemDto> getItemsBySearch(String text);

    CommentDto createComment(CommentDto commentDto, Long userId, Long itemId);
}
