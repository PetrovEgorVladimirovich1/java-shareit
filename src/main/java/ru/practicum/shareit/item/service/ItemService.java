package ru.practicum.shareit.item.service;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Long userId, Item item, BindingResult bindingResult);

    Item update(Long itemId, Long userId, Item item);

    List<ItemWithBookingDto> getItems(Long userId);

    Item getByIdItem(Long itemId, Long userId);

    List<Item> getItemsBySearch(String text);

    CommentDto createComment(Comment comment, Long userId, Long itemId, BindingResult bindingResult);
}
