package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private long id;

    @NotNull
    @NotBlank
    private String name;

    @Size(max=200)
    private String description;

    @Past
    private LocalDate releaseDate;
    private  Duration duration;
}
