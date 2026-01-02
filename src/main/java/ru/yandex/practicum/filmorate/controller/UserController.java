package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findAll() {
        log.info("Get list users");
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        log.info("User creation request: {}", user.toString());
        return userService.create(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody User newUser) {
       log.info("User update request: {}", newUser.toString());
        return userService.update(newUser);
    }

    @PutMapping("/{userId}/friends/{friendUserId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendUserId) {
        log.info("User {} adding friend {}", userId, friendUserId);
        userService.addFriend(userId, friendUserId);
    }

    @DeleteMapping("/{userId}/friends/{friendUserId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendUserId) {
        log.info("User {} removing friend {}", userId, friendUserId);
        userService.removeFriend(userId, friendUserId);
    }

    @GetMapping("/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriends(@PathVariable Long userId) {
        log.info("Get friends for user {}", userId);
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherUserId) {
        log.info("Get common friends for users {} and {}", userId, otherUserId);
        return userService.getCommonFriends(userId, otherUserId);
    }

}
