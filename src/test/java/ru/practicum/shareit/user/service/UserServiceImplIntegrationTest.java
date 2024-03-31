package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceImplIntegrationTest {
    @Autowired
    private UserController userController;

    @Test
    void integrationTest() {
        UserDto userDto = userController.create(new UserDto(null, "name", "egich-2014@mail.ru"));
        assertEquals(userDto, userController.getByIdUser(userDto.getId()));
        assertEquals(1, userController.getUsers().size());
        userDto.setName("NAME");
        assertEquals(userController.update(userDto.getId(), userDto), userController.getByIdUser(userDto.getId()));
        userController.deleteUser(userDto.getId());
        assertEquals(0, userController.getUsers().size());
        assertThrows(FailIdException.class, () -> userController.update(0, userDto));
        assertThrows(FailIdException.class, () -> userController.getByIdUser(0));
    }
}