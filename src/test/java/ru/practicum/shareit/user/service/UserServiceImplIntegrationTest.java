package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceImplIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void integrationTest() {
        userRepository.deleteAll();
        UserDto userDto = userService.create(new UserDto(null, "name", "egich-2014@mail.ru"));
        assertEquals(userDto, userService.getByIdUser(userDto.getId()));
        assertEquals(1, userService.getUsers().size());
        userDto.setName("NAME");
        assertEquals(userService.update(userDto.getId(), userDto), userService.getByIdUser(userDto.getId()));
        userService.deleteUser(userDto.getId());
        assertEquals(0, userService.getUsers().size());
    }
}