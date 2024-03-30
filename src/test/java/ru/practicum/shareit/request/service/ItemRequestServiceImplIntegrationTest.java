package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ItemRequestServiceImplIntegrationTest {
    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void integrationTest() {
        itemRequestRepository.deleteAll();
        UserDto userDto = userService.create(new UserDto(null, "name", "egich-2015@mail.ru"));
        itemRequestService.create(userDto.getId(), new ItemRequestDto(null, "description", null,
                null));
        assertEquals(1, itemRequestService.getItemRequests(userDto.getId()).size());
    }
}