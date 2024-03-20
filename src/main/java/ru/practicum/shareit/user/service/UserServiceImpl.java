package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        log.info("Пользователь успешно создан. {}", user);
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User userLast = UserMapper.toUser(getByIdUser(userId));
        user.setId(userId);
        if (user.getEmail() == null) {
            user.setEmail(userLast.getEmail());
        }
        if (user.getName() == null) {
            user.setName(userLast.getName());
        }
        log.info("Пользователь успешно обновлён. {}", user);
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    public List<UserDto> getUsers() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getByIdUser(long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new FailIdException("Неверный id!");
        }
        return UserMapper.toUserDto(user.get());
    }

    @Override
    public void deleteUser(long id) {
        repository.deleteById(id);
        log.info("Пользователь успешно удалён.");
    }
}
