package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, BookingDto bookingDto);

    BookingDto update(Long bookingId, Long userId, Boolean approved);

    List<BookingDto> getBookings(Long userId, String state);

    BookingDto getByIdBooking(Long bookingId, Long userId);

    List<BookingDto> getForItemsBookings(Long userId, String state);
}