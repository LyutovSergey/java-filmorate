package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    private final Set<Long> friendsId = new HashSet<>();

    public void calculateUserName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
    }

    public void addFriend(Long userId) {
        friendsId.add(userId);
    }

    public void removeFriend(Long userId) {
        friendsId.remove(userId);
    }

}
