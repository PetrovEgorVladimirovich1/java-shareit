package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Entity
@Table(name = "bookings", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    private Long id;

    @Column(name = "start_date")
    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @Column(name = "end_date")
    @NotNull
    @Future
    private LocalDateTime end;

    @Column(name = "item_id")
    @NotNull
    @Positive
    private Long itemId;

    @Column(name = "booker_id")
    @Positive
    private Long booker;

    @Enumerated(EnumType.STRING)
    private Status status;
}
