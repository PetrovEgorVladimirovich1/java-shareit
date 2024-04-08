package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.FailIdException;
import ru.practicum.shareit.exceptions.ValidationExceptionRun;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
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
        throw new ValidationExceptionRun("Unknown state: " + state);
    }

    @Override
    public BookingDto create(Long userId, BookingDto bookingDto) {
        Booking booking = BookingMapper.toBooking(bookingDto);
        Item item = ItemMapper.toItem(itemService.getByIdItem(booking.getItemId(), userId));
        if (item.getOwner().equals(userId)) {
            throw new FailIdException("Нельзя создать бронирование на свою вещь!");
        }
        if (!item.getAvailable()) {
            throw new ValidationExceptionRun("Бронирование не доступно!");
        }
        if (booking.getEnd().isBefore(booking.getStart())
                || booking.getEnd().equals(booking.getStart())) {
            throw new ValidationExceptionRun("Не верная дата окочания бронирования!");
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
            throw new ValidationExceptionRun("Нельзя изменять статус после утверждения!");
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
                ItemMapper.toItem(itemService.getByIdItem(booking.getItemId(), userId)));
    }

    @Override
    public List<BookingDto> getBookings(Long userId, String state, int from, int size) {
        userService.getByIdUser(userId);
        List<BookingDto> bookings = repository.findByBooker(userId,
                        PageRequest.of(from / size, size, Sort.by("start").descending()))
                .stream()
                .map(booking -> BookingMapper.toBookingDto(booking,
                        ItemMapper.toItem(itemService.getByIdItem(booking.getItemId(), userId))))
                .collect(Collectors.toList());
        return getBookingsState(bookings, state);
    }

    @Override
    public BookingDto getByIdBooking(Long bookingId, Long userId) {
        Optional<Booking> booking = repository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new FailIdException("Неверный id!");
        }
        Item item = ItemMapper.toItem(itemService.getByIdItem(booking.get().getItemId(), userId));
        if (!item.getOwner().equals(userId) && !booking.get().getBooker().equals(userId)) {
            throw new FailIdException("Неверный id!");
        }
        return BookingMapper.toBookingDto(booking.get(), item);
    }

    @Override
    public List<BookingDto> getForItemsBookings(Long userId, String state, int from, int size) {
        userService.getByIdUser(userId);
        List<BookingDto> bookings = repository.getForItemsBookings(userId,
                        PageRequest.of(from / size, size, Sort.by("start_date").descending())).stream()
                .map(booking -> BookingMapper.toBookingDto(booking,
                        ItemMapper.toItem(itemService.getByIdItem(booking.getItemId(), userId))))
                .collect(Collectors.toList());
        return getBookingsState(bookings, state);
    }
}
