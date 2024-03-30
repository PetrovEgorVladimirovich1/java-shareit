package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemServiceImplIntegrationTest {
    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void integrationTest() {
        itemRepository.deleteAll();
        userService.create(new UserDto(null, "name", "egich-2013@mail.ru"));
        itemService.create(1L, new ItemDto(null, "name", "description", true,
                null));
        assertEquals(1, itemService.getItems(1L, 0, 1).size());
    }
}