package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemServiceImplIntegrationTest {
    @Autowired
    private ItemController itemController;

    @Autowired
    private UserController userController;

    @Test
    void integrationTest() {
        UserDto userDto = userController.create(new UserDto(null, "name", "egich-2013@mail.ru"));
        itemController.create(new ItemDto(null, "name", "description", true, null),
                userDto.getId());
        assertEquals(1, itemController.getItems(userDto.getId(), 0, 1).size());
        assertThrows(FailIdException.class, () -> itemController.getByIdItem(0L, userDto.getId()));
    }
}