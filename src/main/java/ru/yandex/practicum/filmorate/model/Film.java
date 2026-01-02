package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.DateEqualOrAfter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Long id;

    @NotBlank(message = "name не может быть пустым")
    private String name;

    @Size(max = 200, message = "description не может превышать 200 символов")
    private String description;

    @Past(message = "releaseDate не может быть из будущего")
    @DateEqualOrAfter(value = "1895-12-28", message = "releaseDate может быть не раньше 1895-12-28")
    private LocalDate releaseDate;

    @Positive(message = "duration не может быть отрицательным")
    private int duration; // Судя по коллекции, предполагалось использовать именно числовое значение

    Set<Long> userIdLikes = new HashSet<>();

    public void addUserLike(Long userId) {
        userIdLikes.add(userId);
    }

    public void removeUserLike(Long userId) {
        userIdLikes.remove(userId);
    }

}
