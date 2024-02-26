package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validate.Validate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Map<Long, Item>> userItems = new HashMap<>();

    private final Map<Long, Item> items = new HashMap<>();

    private final UserService userService;

    @Autowired
    public ItemServiceImpl(UserService userService) {
        this.userService = userService;
    }

    private long id = 0;

    private long addId() {
        return ++id;
    }

    private boolean isFilter(Item item, String text) {
        return item.getAvailable()
                && (item.getName().toLowerCase().contains(text.toLowerCase())
                || item.getDescription().toLowerCase().contains(text.toLowerCase()));
    }

    @Override
    public Item create(Long userId, Item item, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        userService.getByIdUser(userId);
        item.setId(addId());
        item.setOwner(userId);
        if (userItems.containsKey(userId)) {
            userItems.get(userId).put(item.getId(), item);
            items.put(item.getId(), item);
        } else {
            Map<Long, Item> itemMap = new HashMap<>();
            itemMap.put(item.getId(), item);
            userItems.put(userId, itemMap);
            items.put(item.getId(), item);
        }
        log.info("Вещь успешно добавлена. {}", item);
        return item;
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
        userItems.get(userId).put(item.getId(), item);
        items.put(item.getId(), item);
        log.info("Вещь успешно обновлена. {}", item);
        return item;
    }

    @Override
    public List<Item> getItems(Long userId) {
        if (!userItems.containsKey(userId)) {
            throw new FailIdException("Неверный id!");
        }
        return new ArrayList<>(userItems.get(userId).values());
    }

    @Override
    public Item getByIdItem(Long itemId, Long userId) {
        if (!items.containsKey(itemId)) {
            throw new FailIdException("Неверный id!");
        }
        return items.get(itemId);
    }

    @Override
    public List<Item> getItemsBySearch(String text) {
        List<Item> itemList = new ArrayList<>();
        if (!text.isBlank()) {
            itemList.addAll(items.values().stream().filter(item -> isFilter(item, text))
                    .collect(Collectors.toList()));
        }
        return itemList;

    }
}
