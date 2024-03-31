package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
@Table(name = "requests", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    private Long id;

    @NotBlank
    @Size(max = 512)
    private String description;

    @Column(name = "requestor_id")
    @Positive
    private Long requestor;

    private LocalDateTime created;
}
