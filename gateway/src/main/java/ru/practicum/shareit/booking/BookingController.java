package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingClientDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from",
                                                      defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, stateParam, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookingClientDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Positive @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@Positive @PathVariable Long bookingId,
                                         @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(name = "approved") Boolean approved) {
        log.info("Update bookingId {}, userId={}, approved={}", bookingId, userId, approved);
        return bookingClient.update(bookingId, userId, approved);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getForItemsBookings(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(name = "state",
                                                              defaultValue = "ALL") String stateParam,
                                                      @RequestParam(name = "from", defaultValue = "0")
                                                      @PositiveOrZero int from,
                                                      @RequestParam(name = "size", defaultValue = "10")
                                                      @Positive int size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getForItemsBookings(userId, stateParam, from, size);
    }
}