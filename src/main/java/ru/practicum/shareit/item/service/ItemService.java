package ru.practicum.shareit.item.service;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Long userId, Item item, BindingResult bindingResult);

    Item update(Long itemId, Long userId, Item item);

    List<Item> getItems(Long userId);

    Item getByIdItem(Long itemId, Long userId);

    List<Item> getItemsBySearch(String text);
}
