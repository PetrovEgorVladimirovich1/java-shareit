package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingForItemDto;

import javax.validation.constraints.Positive;
import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemWithBookingDto {
    @Positive
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingForItemDto lastBooking;

    private BookingForItemDto nextBooking;

    private List<CommentDto> comments;

    @Positive
    private Long owner;

    @Positive
    private Long requestId;
}
