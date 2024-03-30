package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
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
                                        @RequestParam(name = "state", defaultValue = "ALL", required = false)
                                        String state,
                                        @RequestParam(name = "from", defaultValue = "0")
                                        @PositiveOrZero int from,
                                        @RequestParam(name = "size", defaultValue = "10")
                                        @Positive int size) {
        return bookingService.getBookings(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getByIdBooking(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getByIdBooking(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getForItemsBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL", required = false)
                                                String state,
                                                @RequestParam(name = "from", defaultValue = "0")
                                                @PositiveOrZero int from,
                                                @RequestParam(name = "size", defaultValue = "10")
                                                @Positive int size) {
        return bookingService.getForItemsBookings(userId, state, from, size);
    }
}
