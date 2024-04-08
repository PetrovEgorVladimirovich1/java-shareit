package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.exceptions.ValidationExceptionRun;
import ru.practicum.shareit.item.dto.ItemForBookingDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserBookerDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, userService, itemService);
    }

    @Test
    void create() {
        BookingDto bookingDto = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1),
                Status.WAITING, new UserBookerDto(2L), new ItemForBookingDto(1L, "name"),
                1L, 2L);
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "name", "description",
                true, null, null, null, 1L, 1L);
        Mockito.when(itemService.getByIdItem(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemWithBookingDto);
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(BookingMapper.toBooking(bookingDto));
        assertEquals(bookingDto, bookingService.create(2L, bookingDto));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(BookingMapper.toBooking(bookingDto));

        assertThrows(FailIdException.class, () -> bookingService.create(1L, bookingDto));

        itemWithBookingDto.setAvailable(false);
        Mockito.when(itemService.getByIdItem(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemWithBookingDto);
        assertThrows(ValidationExceptionRun.class, () -> bookingService.create(2L, bookingDto));

        itemWithBookingDto.setAvailable(true);
        bookingDto.setEnd(bookingDto.getStart());
        assertThrows(ValidationExceptionRun.class, () -> bookingService.create(2L, bookingDto));

        bookingDto.setEnd(LocalDateTime.now().minusDays(2));
        assertThrows(ValidationExceptionRun.class, () -> bookingService.create(2L, bookingDto));
    }

    @Test
    void update() {
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L, 2L,
                Status.WAITING);
        Booking booking1 = new Booking(1L, booking.getStart(), booking.getEnd(), 1L, 2L, Status.WAITING);
        BookingDto bookingDto = new BookingDto(1L, booking.getStart(), booking.getEnd(),
                Status.APPROVED, new UserBookerDto(2L),
                new ItemForBookingDto(1L, "name"), 1L, 2L);
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "name", "description",
                true, null, null, null, 1L, 1L);
        Mockito.when(itemService.getByIdItem(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemWithBookingDto);
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking1));
        booking.setStatus(Status.APPROVED);
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
        assertEquals(bookingDto, bookingService.update(1L, 1L, true));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(booking);

        booking.setStatus(Status.REJECTED);
        bookingDto.setStatus(Status.REJECTED);
        assertEquals(bookingDto, bookingService.update(1L, 1L, false));

        assertThrows(FailIdException.class, () -> bookingService.update(1L, 2L, true));

        booking1.setStatus(Status.APPROVED);
        assertThrows(ValidationExceptionRun.class, () -> bookingService.update(1L, 1L, true));
    }

    @Test
    void getBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(1L, LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusDays(1), 1L, 2L, Status.WAITING));
        bookings.add(new Booking(1L, LocalDateTime.now().minusHours(3),
                LocalDateTime.now().plusHours(2), 1L, 2L, Status.WAITING));
        Mockito.when(userService.getByIdUser(Mockito.anyLong())).thenReturn(new UserDto());
        Mockito.when(bookingRepository.findByBooker(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .thenReturn(bookings);
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "name", "description",
                true, null, null, null, 1L, 1L);
        Mockito.when(itemService.getByIdItem(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemWithBookingDto);
        assertEquals(2, bookingService.getBookings(1L, "ALL", 1, 1).size());
        assertEquals(2, bookingService.getBookings(1L, "CURRENT", 1, 1).size());
        assertTrue(bookingService.getBookings(1L, "PAST", 1, 1).isEmpty());
        assertTrue(bookingService.getBookings(1L, "FUTURE", 1, 1).isEmpty());
        assertEquals(2, bookingService.getBookings(1L, "WAITING", 1, 1).size());
        assertTrue(bookingService.getBookings(1L, "REJECTED", 1, 1).isEmpty());
        assertThrows(ValidationExceptionRun.class,
                () -> bookingService.getBookings(1L, "Unknown state", 1, 1));
    }

    @Test
    void getByIdBooking() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(FailIdException.class, () -> bookingService.getByIdBooking(1L, 1L));

        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L, 2L,
                Status.WAITING);
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "name", "description",
                true, null, null, null, 1L, 1L);
        Mockito.when(itemService.getByIdItem(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemWithBookingDto);
        assertThrows(FailIdException.class, () -> bookingService.getByIdBooking(1L, 3L));
    }

    @Test
    void getForItemsBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L, 2L,
                Status.WAITING));
        bookings.add(new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(2), 1L, 2L,
                Status.WAITING));
        Mockito.when(userService.getByIdUser(Mockito.anyLong())).thenReturn(new UserDto());
        Mockito.when(bookingRepository.getForItemsBookings(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .thenReturn(bookings);
        ItemWithBookingDto itemWithBookingDto = new ItemWithBookingDto(1L, "name", "description",
                true, null, null, null, 1L, 1L);
        Mockito.when(itemService.getByIdItem(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemWithBookingDto);
        assertEquals(2, bookingService.getForItemsBookings(1L, "ALL", 1, 1).size());
    }
}