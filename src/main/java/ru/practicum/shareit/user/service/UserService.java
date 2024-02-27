package ru.practicum.shareit.user.service;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User create(User user, BindingResult bindingResult);

    User update(Long userId, User user);

    List<User> getUsers();

    User getByIdUser(long id);

    void deleteUser(long id);
}
