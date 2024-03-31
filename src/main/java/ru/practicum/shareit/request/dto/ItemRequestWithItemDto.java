package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestWithItemDto {
    @Positive
    private Long id;

    @NotBlank
    @Size(max = 512)
    private String description;

    @Positive
    private Long requestor;

    private LocalDateTime created;

    private List<Item> items;
}
