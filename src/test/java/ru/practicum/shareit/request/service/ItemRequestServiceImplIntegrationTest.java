package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemRequestServiceImplIntegrationTest {
    @Autowired
    private ItemRequestController itemRequestController;

    @Autowired
    private UserController userController;

    @Test
    void integrationTest() {
        UserDto userDto = userController.create(new UserDto(null, "name", "egich-2015@mail.ru"));
        itemRequestController.create(new ItemRequestDto(null, "description", null,
                null), userDto.getId());
        assertEquals(1, itemRequestController.getItemRequests(userDto.getId()).size());
        assertThrows(FailIdException.class, () -> itemRequestController.getByIdItemRequest(userDto.getId(), 0L));
    }
}