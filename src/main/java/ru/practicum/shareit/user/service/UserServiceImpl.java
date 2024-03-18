package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validate.Validate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User create(User user, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        log.info("Пользователь успешно создан. {}", user);
        return repository.save(user);
    }

    @Override
    public User update(Long userId, User user) {
        User userLast = getByIdUser(userId);
        user.setId(userId);
        if (user.getEmail() == null) {
            user.setEmail(userLast.getEmail());
        }
        if (user.getName() == null) {
            user.setName(userLast.getName());
        }
        log.info("Пользователь успешно обновлён. {}", user);
        return repository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return repository.findAll();
    }

    @Override
    public User getByIdUser(long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new FailIdException("Неверный id!");
        }
        return user.get();
    }

    @Override
    public void deleteUser(long id) {
        repository.deleteById(id);
        log.info("Пользователь успешно удалён.");
    }
}
