package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    protected final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Get list users");
        log.debug("{}", users.values());
        return users.values();
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.info("User creation request: {}", user.toString());
        if (isDuplicateEmail(user.getEmail())) {
            log.info("User creation failed! email={} is already in use", user.getEmail());
            throw new DuplicatedDataException("This email is already in use");
        }
        user.calculateUserName();
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("User created!");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
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
            log.info("User update request failed! Id is null");
            throw new ConditionsNotMetException("Id must be specified");
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (!oldUser.getEmail().equals(newUser.getEmail())) {
                if (isDuplicateEmail(newUser.getEmail())) {
                    log.info("User update request failed! email={} is already in use", newUser.getEmail());
                    throw new DuplicatedDataException("This email is already in use");
                }
            }
            newUser.calculateUserName();
            users.put(newUser.getId(), newUser);
            log.info("User updated!");
            return newUser;
        }
        log.info("User update request failed! id={} not found", newUser.getId());
        throw new NotFoundException("User id = " + newUser.getId() + " not found");
    }

    boolean isDuplicateEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().orElse(null) != null;
    }
}
