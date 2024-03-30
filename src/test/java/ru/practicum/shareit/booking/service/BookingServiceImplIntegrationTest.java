package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookingServiceImplIntegrationTest {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void integrationTest() {
        bookingRepository.deleteAll();
        userService.create(new UserDto(null, "name", "egich-2011@mail.ru"));
        userService.create(new UserDto(null, "name", "egich-2012@mail.ru"));
        itemService.create(1L, new ItemDto(null, "name", "description", true,
                null));
        bookingService.create(2L, new BookingDto(null, LocalDateTime.now().plusSeconds(2), LocalDateTime.now().plusDays(1),
                null, null, null, 1L, null));
        assertEquals(1, bookingService.getBookings(2L, "ALL", 0, 1).size());
    }
}