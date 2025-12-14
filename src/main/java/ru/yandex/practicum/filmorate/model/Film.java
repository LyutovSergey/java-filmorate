package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.DateEqualOrAfter;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;

    @NotNull(message = "name не может быть null")
    @NotBlank(message = "name не может быть пустым")
    private String name;

    @Size(max=200,message = "description не может превышать 200 символов")
    private String description;

    @Past(message = "releaseDate не может быть из будущего")
    @DateEqualOrAfter(value = "28.12.1895", message = "releaseDate может быть не раньше 28.12.1895")
    private LocalDate releaseDate;

    @Positive(message = "duration не может быть отрицательным")
    private  Duration duration;
}
