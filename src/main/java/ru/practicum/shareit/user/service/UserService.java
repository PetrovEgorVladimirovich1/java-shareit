package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(Long userId, UserDto userDto);

    List<UserDto> getUsers();

    UserDto getByIdUser(long id);

    void deleteUser(long id);
}
