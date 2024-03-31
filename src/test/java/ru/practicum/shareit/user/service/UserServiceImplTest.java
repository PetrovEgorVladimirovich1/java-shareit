package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void create() {
        UserDto userDto = new UserDto(1L, "name", "egich-2011@mail.ru");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(UserMapper.toUser(userDto));
        assertEquals(userDto, userService.create(userDto));
    }

    @Test
    void update() {
        UserDto userDto = new UserDto(1L, "name", null);
        User user = new User(1L, "name", "egich-2011@mail.ru");
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        assertEquals(UserMapper.toUserDto(user), userService.update(1L, userDto));
        userDto.setEmail("egich-2011@mail.ru");
        userDto.setName(null);
        assertEquals(UserMapper.toUserDto(user), userService.update(1L, userDto));
    }

    @Test
    void getUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(userService.getUsers().isEmpty());
    }

    @Test
    void getByIdUser() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(FailIdException.class, () -> userService.getByIdUser(1L));
    }

    @Test
    void deleteUser() {
        userService.deleteUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}