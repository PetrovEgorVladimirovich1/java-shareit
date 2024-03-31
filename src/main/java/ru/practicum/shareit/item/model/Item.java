package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Entity
@Table(name = "items", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 512)
    private String description;

    @NotNull
    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "owner_id")
    @Positive
    private Long owner;

    @Positive
    @Column(name = "request_id")
    private Long requestId;
}
