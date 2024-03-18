package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static ItemForBookingDto toItemDtoBooking(Item item) {
        return new ItemForBookingDto(item.getId(), item.getName());
    }

    public static ItemWithBookingDto toItemWithBookingDto(Item item,
                                                          Booking lastBooking,
                                                          Booking nextBooking,
                                                          List<CommentDto> commentDtos) {
        return new ItemWithBookingDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                BookingMapper.toBookingForItemDto(lastBooking),
                BookingMapper.toBookingForItemDto(nextBooking),
                new ArrayList<>(commentDtos)
        );
    }

    public static CommentDto toCommentDto(Comment comment){
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                LocalDateTime.now());
    }
}
