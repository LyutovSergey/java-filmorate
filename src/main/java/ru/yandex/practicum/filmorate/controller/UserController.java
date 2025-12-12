package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")

public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        // проверяем выполнение необходимых условий
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (users.values().stream().filter(
                        tempUser -> tempUser
                                .getEmail()
                                .equals(user.getEmail()))
                .findFirst().orElse(null) != null
        ) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        // формируем дополнительные данные
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (!oldUser.getEmail().equals(newUser.getEmail())) {
                if (users.values().stream().filter(
                                tempUser -> tempUser
                                        .getEmail()
                                        .equals(newUser.getEmail()))
                        .findFirst().orElse(null) != null
                ) {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                }
            }

            if (newUser.getEmail() == null || newUser.getPassword() == null) {
                //throw new ConditionsNotMetException("Описание не может быть пустым");
                // Оставляем старые учетные данные
                newUser.setEmail(oldUser.getEmail());
                newUser.setPassword(oldUser.getPassword());
            }
            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            oldUser = newUser.toBuilder().build();
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }
}
