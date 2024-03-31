package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
public class ItemRequestDto {
    @Positive
    private Long id;

    @NotBlank
    @Size(max = 512)
    private String description;

    @Positive
    private Long requestor;

    private LocalDateTime created;
}
