package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingDto bookingDto,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam(name = "approved") Boolean approved) {
        return bookingService.update(bookingId, userId, approved);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(name = "state", defaultValue = "ALL", required = false) String state) {
        return bookingService.getBookings(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getByIdBooking(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getByIdBooking(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getForItemsBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL", required = false) String state) {
        return bookingService.getForItemsBookings(userId, state);
    }
}
