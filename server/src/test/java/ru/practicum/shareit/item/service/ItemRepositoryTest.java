package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user;

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(null, "name", "egich-2011@mail.ru"));
        itemRequest = itemRequestRepository.save(new ItemRequest(null, "description",
                user.getId(), LocalDateTime.now()));
    }

    @Test
    void findByRequestId() {
        assertTrue(itemRepository.findByRequestId(itemRequest.getId()).isEmpty());
        itemRepository.save(new Item(null, "name",
                "description", true, user.getId(), itemRequest.getId()));
        assertEquals(1, itemRepository.findByRequestId(itemRequest.getId()).size());
    }

    @Test
    void findByOwner() {
        assertTrue(itemRepository.findByOwner(user.getId(), PageRequest.of(0, 1)).isEmpty());
        itemRepository.save(new Item(null, "name",
                "description", true, user.getId(), itemRequest.getId()));
        assertEquals(1, itemRepository.findByOwner(user.getId(), PageRequest.of(0, 1)).size());
    }

    @Test
    void findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue() {
        assertTrue(itemRepository
                .findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue("",
                        "", PageRequest.of(0, 1)).isEmpty());
        itemRepository.save(new Item(null, "name",
                "description", true, user.getId(), itemRequest.getId()));
        assertEquals(1, itemRepository
                .findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndAvailableTrue("name",
                        "description", PageRequest.of(0, 1)).size());
    }
}