package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    @NotNull
    private Boolean available;


    private Long owner;


    private ItemRequest request;
}
