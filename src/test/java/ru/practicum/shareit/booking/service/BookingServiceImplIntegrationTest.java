package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.exceptions.ValidationExceptionRun;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserController;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BookingServiceImplIntegrationTest {
    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    @Test
    void integrationTest() {
        UserDto userDto = userController.create(new UserDto(null, "name", "egich-2011@mail.ru"));
        UserDto userDto1 = userController.create(new UserDto(null, "name", "egich-2012@mail.ru"));
        ItemDto itemDto = itemController.create(new ItemDto(null, "name", "description", true,
                null), userDto.getId());
        BookingDto bookingDto = bookingController.create(new BookingDto(null, LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusDays(1),null, null, null, itemDto.getId(), null),
                userDto1.getId());
        assertEquals(1, bookingController.getBookings(userDto1.getId(), "ALL", 0, 1).size());
        assertThrows(FailIdException.class, () -> bookingController.create(bookingDto, userDto.getId()));
        itemDto.setAvailable(false);
        itemController.update(itemDto.getId(), userDto.getId(), itemDto);
        assertThrows(ValidationExceptionRun.class, () -> bookingController.create(bookingDto, userDto1.getId()));
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(4));
        itemDto.setAvailable(true);
        itemController.update(itemDto.getId(), userDto.getId(), itemDto);
        assertThrows(ValidationExceptionRun.class, () -> bookingController.create(bookingDto, userDto1.getId()));
    }
}