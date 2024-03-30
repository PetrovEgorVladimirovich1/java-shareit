package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private BookingRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Long userId;

    private Long userId1;

    private Long itemId;

    @BeforeEach
    void setUp() {
        userId = userRepository.save(new User(null, "name", "egich-2011@mail.ru")).getId();
        userId1 = userRepository.save(new User(null, "name", "egich-2012@mail.ru")).getId();
        itemId = itemRepository.save(new Item(null, "name",
                "description", true, userId, null)).getId();
    }

    @Test
    void getForItemsBookings() {
        List<Booking> bookings = repository.getForItemsBookings(userId, PageRequest.of(0, 1));
        assertTrue(bookings.isEmpty());
        Booking booking = new Booking(null, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1), itemId, userId1, Status.WAITING);
        repository.save(booking);
        bookings = repository.getForItemsBookings(userId, PageRequest.of(0, 1));
        assertEquals(1, bookings.size());
    }

    @Test
    void getLastBooking() {
        Booking booking = repository.getLastBooking(userId, itemId, LocalDateTime.now());
        assertNull(booking);
        booking = new Booking(null, LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusDays(1), itemId, userId1, Status.APPROVED);
        repository.save(booking);
        assertEquals(booking, repository.getLastBooking(userId, itemId, LocalDateTime.now().plusSeconds(2)));
    }

    @Test
    void getNextBooking() {
        Booking booking = repository.getNextBooking(userId, LocalDateTime.now(), itemId);
        assertNull(booking);
        booking = new Booking(null, LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusDays(1), itemId, userId1, Status.APPROVED);
        repository.save(booking);
        assertEquals(booking, repository.getNextBooking(userId, LocalDateTime.now(), itemId));
    }

    @Test
    void findByBookerAndStatusAndEndBefore() {
        List<Booking> bookings = repository.findByBookerAndStatusAndEndBefore(userId1, Status.APPROVED, LocalDateTime.now());
        assertTrue(bookings.isEmpty());
        Booking booking = new Booking(null, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1), itemId, userId1, Status.WAITING);
        repository.save(booking);
        bookings = repository.findByBookerAndStatusAndEndBefore(userId1, Status.WAITING, LocalDateTime.now().plusDays(1));
        assertEquals(1, bookings.size());
    }

    @Test
    void findByBooker() {
        List<Booking> bookings = repository.findByBooker(userId1, PageRequest.of(0, 1));
        assertTrue(bookings.isEmpty());
        Booking booking = new Booking(null, LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(1), itemId, userId1, Status.WAITING);
        repository.save(booking);
        bookings = repository.findByBooker(userId1, PageRequest.of(0, 1));
        assertEquals(1, bookings.size());
    }
}