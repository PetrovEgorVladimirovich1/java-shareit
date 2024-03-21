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
                new ArrayList<>(commentDtos),
                item.getOwner()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                LocalDateTime.now());
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                null);
    }

    public static Item toItem(ItemWithBookingDto itemWithBookingDto) {
        return new Item(itemWithBookingDto.getId(),
                itemWithBookingDto.getName(),
                itemWithBookingDto.getDescription(),
                itemWithBookingDto.getAvailable(),
                itemWithBookingDto.getOwner(),
                null);
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                null,
                null);
    }
}
