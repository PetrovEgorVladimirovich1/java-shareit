package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserBookerDto;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking, Item item) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                new UserBookerDto(booking.getBooker()),
                ItemMapper.toItemDtoBooking(item),
                booking.getItemId(),
                booking.getBooker()
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItemId(),
                bookingDto.getBookerId(),
                bookingDto.getStatus()
        );
    }

    public static BookingForItemDto toBookingForItemDto(Booking booking) {
        if (booking != null) {
            return new BookingForItemDto(booking.getId(), booking.getBooker());
        }
        return null;
    }
}
