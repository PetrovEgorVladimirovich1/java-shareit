package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(null, "name", "egich-2011@mail.ru"));
    }

    @Test
    void findByRequestorOrderByCreatedDesc() {
        assertTrue(itemRequestRepository.findByRequestorOrderByCreatedDesc(user.getId()).isEmpty());
        itemRequestRepository.save(new ItemRequest(null, "decription", user.getId(), LocalDateTime.now()));
        assertEquals(1, itemRequestRepository.findByRequestorOrderByCreatedDesc(user.getId()).size());
    }

    @Test
    void findByRequestorNot() {
        User user1 = userRepository.save(new User(null, "name", "egich-2012@mail.ru"));
        itemRequestRepository.save(new ItemRequest(null, "decription", user.getId(), LocalDateTime.now()));
        assertTrue(itemRequestRepository.findByRequestorNot(user.getId(), PageRequest.of(0, 1)).isEmpty());
        itemRequestRepository.save(new ItemRequest(null, "decription", user1.getId(), LocalDateTime.now()));
        assertEquals(1, itemRequestRepository.findByRequestorNot(user.getId(),
                PageRequest.of(0, 1)).size());
    }

    @Test
    void findByIdAndRequestor() {
        assertNull(itemRequestRepository.findByIdAndRequestor(1L, user.getId()));
        ItemRequest itemRequest = itemRequestRepository.save(new ItemRequest(null, "decription", user.getId(), LocalDateTime.now()));
        assertEquals(itemRequest, itemRequestRepository.findByIdAndRequestor(itemRequest.getId(), user.getId()));
    }
}