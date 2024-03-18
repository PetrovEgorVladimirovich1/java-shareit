package ru.practicum.shareit.booking.service;

import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Arrays;
import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, Booking booking, BindingResult bindingResult);

    BookingDto update(Long bookingId, Long userId, Boolean approved);

    List<BookingDto> getBookings(Long userId, String state);

    BookingDto getByIdBooking(Long bookingId, Long userId);

    List<BookingDto> getForItemsBookings(Long userId, String state);
}