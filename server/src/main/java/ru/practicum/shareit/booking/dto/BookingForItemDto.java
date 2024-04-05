package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class BookingForItemDto {
    @Positive
    private Long id;
    @Positive
    private Long bookerId;
}
