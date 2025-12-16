package ru.yandex.practicum.filmorate.model;



import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    @Email(message = "email некорректный")
    private String email;

    @NotBlank(message = "login не может быть пустым")
    private String login;

    private String name;

    @Past(message = "birthday не может быть из будущего")
    @NotNull(message = "birthday не может быть null")
    private LocalDate birthday;

    public void calculateUserName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
    }
}
