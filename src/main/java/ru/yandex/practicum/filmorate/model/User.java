package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private Long id;

    @Email(message = "email некорректный")
    private String email;

    @NotNull(message = "login не может быть null")
    @NotBlank(message = "login не может быть пустым")
    private String login;

    private String name;

    @Past(message = "birthday не может быть из будущего")
    private LocalDate birthday;
}
