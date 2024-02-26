package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.exceptions.NotEmailException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validate.Validate;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();

    private final Set<String> emails = new HashSet<>();

    private long id = 0;

    private long addId() {
        return ++id;
    }

    @Override
    public User create(User user, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        if (!emails.add(user.getEmail())) {
            throw new NotEmailException("Такой email уже есть!");
        }
        user.setId(addId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан. {}", user);
        return user;
    }

    @Override
    public User update(Long userId, User user) {
        if (user.getEmail() != null
                && !getByIdUser(userId).getEmail().equals(user.getEmail())
                && emails.contains(user.getEmail())) {
            throw new NotEmailException("Такой email уже есть!");
        }
        if (user.getEmail() == null) {
            user.setEmail(users.get(userId).getEmail());
        }
        if (user.getName() == null) {
            user.setName(users.get(userId).getName());
        }
        if (emails.add(user.getEmail())) {
            emails.remove(users.get(userId).getEmail());
        }
        user.setId(userId);
        users.put(userId, user);
        log.info("Пользователь успешно обновлён. {}", user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getByIdUser(long id) {
        if (!users.containsKey(id)) {
            throw new FailIdException("Неверный id!");
        }
        return users.get(id);
    }

    @Override
    public void deleteUser(long id) {
        User user = getByIdUser(id);
        users.remove(id);
        emails.remove(user.getEmail());
    }
}
