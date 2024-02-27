package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody User user, BindingResult bindingResult) {
        return UserMapper.toUserDto(userService.create(user, bindingResult));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId, @RequestBody User user) {
        return UserMapper.toUserDto(userService.update(userId, user));
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getByIdUser(@PathVariable long id) {
        return UserMapper.toUserDto(userService.getByIdUser(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }
}
