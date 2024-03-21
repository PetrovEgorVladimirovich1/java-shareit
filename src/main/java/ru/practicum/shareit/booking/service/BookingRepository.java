package ru.practicum.shareit.booking.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT b.id, " +
            "b.start_date, " +
            "b.end_date, " +
            "b.item_id, " +
            "b.booker_id, " +
            "b.status " +
            "FROM bookings AS b " +
            "LEFT JOIN items AS i ON b.item_id = i.id " +
            "LEFT JOIN users AS u ON i.owner_id = u.id " +
            "WHERE u.id = ?1 ", nativeQuery = true)
    List<Booking> getForItemsBookings(Long userId);

    @Query(value = "SELECT B.ID, " +
            "B.START_DATE, " +
            "B.END_DATE, " +
            "B.ITEM_ID, " +
            "B.BOOKER_ID, " +
            "B.STATUS " +
            "FROM BOOKINGS AS B " +
            "LEFT JOIN ITEMS AS I ON B.ITEM_ID = I.ID " +
            "WHERE I.OWNER_ID = ?1 " +
            "AND B.STATUS = 'APPROVED' " +
            "AND B.ITEM_ID = ?2 " +
            "AND B.START_DATE < ?3 " +
            "ORDER BY B.START_DATE DESC " +
            "LIMIT 1 ", nativeQuery = true)
    Booking getLastBooking(Long userId, Long itemId, LocalDateTime now);

    @Query(value = "SELECT B.ID, " +
            "B.START_DATE, " +
            "B.END_DATE, " +
            "B.ITEM_ID, " +
            "B.BOOKER_ID, " +
            "B.STATUS " +
            "FROM BOOKINGS AS B " +
            "LEFT JOIN ITEMS AS I ON B.ITEM_ID = I.ID " +
            "WHERE I.OWNER_ID = ?1 " +
            "AND B.STATUS = 'APPROVED' " +
            "AND B.START_DATE > ?2 " +
            "AND B.ITEM_ID = ?3 " +
            "ORDER BY B.START_DATE ASC " +
            "LIMIT 1 ", nativeQuery = true)
    Booking getNextBooking(Long userId, LocalDateTime start, Long itemId);

    List<Booking> findByBookerAndStatusAndEndBefore(Long booker, Status status, LocalDateTime end);
}