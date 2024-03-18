package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validate.Validate;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;

    private final UserService userService;

    private final ItemService itemService;

    private List<BookingDto> getBookingsState(List<BookingDto> bookings, String state) {
        if (state.equals("ALL")) {
            return bookings;
        }
        if (state.equals("CURRENT")) {
            return bookings.stream()
                    .filter(booking -> booking.getStart().isBefore(LocalDateTime.now())
                            && booking.getEnd().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }
        if (state.equals("PAST")) {
            return bookings.stream()
                    .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }
        if (state.equals("FUTURE")) {
            return bookings.stream()
                    .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());
        }
        if (state.equals("WAITING")) {
            return bookings.stream()
                    .filter(booking -> booking.getStatus().equals(Status.WAITING))
                    .collect(Collectors.toList());
        }
        if (state.equals("REJECTED")) {
            return bookings.stream()
                    .filter(booking -> booking.getStatus().equals(Status.REJECTED))
                    .collect(Collectors.toList());
        }
        throw new ValidationException("Unknown state: " + state);
    }

    @Override
    public BookingDto create(Long userId, Booking booking, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        Item item = itemService.getByIdItem(booking.getItemId(), userId);
        if (item.getOwner().equals(userId)) {
            throw new FailIdException("Нельзя создать бронирование на свою вещь!");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Бронирование не доступно!");
        }
        if (booking.getEnd().isBefore(booking.getStart())
                || booking.getEnd().equals(booking.getStart())) {
            throw new ValidationException("Не верная дата окочания бронирования!");
        }
        booking.setBooker(userId);
        booking.setStatus(Status.WAITING);
        log.info("Бронирование успешно добавлено. {}", booking);
        return BookingMapper.toBookingDto(repository.save(booking), item);
    }

    @Override
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        Booking booking = BookingMapper.toBooking(getByIdBooking(bookingId, userId));
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Нельзя изменять статус после утверждения!");
        }
        if (booking.getBooker().equals(userId)) {
            throw new FailIdException("Неверный id!");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        log.info("Бронирование успешно обновлено. {}", booking);
        return BookingMapper.toBookingDto(repository.save(booking),
                itemService.getByIdItem(booking.getItemId(), userId));
    }

    @Override
    public List<BookingDto> getBookings(Long userId, String state) {
        userService.getByIdUser(userId);
        List<BookingDto> bookings = repository.findAll().stream()
                .filter(booking -> booking.getBooker().equals(userId))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(booking -> BookingMapper.toBookingDto(booking,
                        itemService.getByIdItem(booking.getItemId(), userId)))
                .collect(Collectors.toList());
        return getBookingsState(bookings, state);
    }

    @Override
    public BookingDto getByIdBooking(Long bookingId, Long userId) {
        Optional<Booking> booking = repository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new FailIdException("Неверный id!");
        }
        Item item = itemService.getByIdItem(booking.get().getItemId(), userId);
        if (!item.getOwner().equals(userId) && !booking.get().getBooker().equals(userId)) {
            throw new FailIdException("Неверный id!");
        }
        return BookingMapper.toBookingDto(repository.save(booking.get()), item);
    }

    @Override
    public List<BookingDto> getForItemsBookings(Long userId, String state) {
        userService.getByIdUser(userId);
        List<BookingDto> bookings = repository.getForItemsBookings(userId).stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(booking -> BookingMapper.toBookingDto(booking,
                        itemService.getByIdItem(booking.getItemId(), userId)))
                .collect(Collectors.toList());
        return getBookingsState(bookings, state);
    }
}
