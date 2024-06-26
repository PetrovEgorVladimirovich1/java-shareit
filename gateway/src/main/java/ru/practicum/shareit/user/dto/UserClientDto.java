package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserClientDto {
    @NotBlank
    @Size(max = 255)
    private String name;

    @Email
    @NotBlank
    @Size(max = 255)
    private String email;
}
