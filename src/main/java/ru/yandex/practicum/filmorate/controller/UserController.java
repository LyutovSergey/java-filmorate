package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Get list users");
        log.debug("{}", users.values().toString());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("User creation request: {}", user.toString());
        if (isDuplicateEmail(user.getEmail())) {
            throw new DuplicatedDataException("This email is already in use");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("User created: {}", user.toString());
        return user;
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("User update request: {}", newUser.toString());
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id must be specified");
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (!oldUser.getEmail().equals(newUser.getEmail())) {
                if (isDuplicateEmail(newUser.getEmail())) {
                    throw new DuplicatedDataException("This email is already in use");
                }
            }
            users.put(newUser.getId(), newUser);
            return newUser;
        }
        throw new NotFoundException("User id = " + newUser.getId() + " not found");
    }

    boolean isDuplicateEmail (String email){
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().orElse(null) != null;
    }
}
