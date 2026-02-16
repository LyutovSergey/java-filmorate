package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.info("User update request failed! Id is null");
            throw new ConditionsNotMetException("Id must be specified");
        }

        Long userId = newUser.getId();
        User oldUser = getUserByIdOrThrow(userId);
        String newUserEmail = newUser.getEmail();

        if (!oldUser.getEmail().equals(newUserEmail)) {
            if (userStorage.isEmailRegistered(newUserEmail)) {
                log.info("User update failed! Email={} is already in use", newUserEmail);
                throw new DuplicatedDataException("This email is already in use");
            }
        }
        return userStorage.update(newUser);
    }

    public User create(User user) {
        if (userStorage.isEmailRegistered(user.getEmail())) {
            log.info("User creation failed! email={} is already in use", user.getEmail());
            throw new DuplicatedDataException("This email is already in use");
        }
        return userStorage.create(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(Long userId, Long friendUserId) {
        userIdRegisteredOrThrow(userId);
        userIdRegisteredOrThrow(friendUserId); // Проверка наличия друга, или 404
        userStorage.addFriend(userId, friendUserId);
    }

    public void removeFriend(Long userId, Long friendUserId) {
        userIdRegisteredOrThrow(userId);
        userIdRegisteredOrThrow(friendUserId); // Проверка наличия друга, или 404
        userStorage.removeFriend(userId, friendUserId);
    }

    public Collection<User> getFriends(Long userId) {
        userIdRegisteredOrThrow(userId);
        return userStorage.getFriends(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        userIdRegisteredOrThrow(userId);
        userIdRegisteredOrThrow(otherUserId);
        return userStorage.getCommonFriends(userId, otherUserId);
    }

    private User getUserByIdOrThrow(Long userId) {
        return userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("User id = " + userId + " not found"));
    }

    private void userIdRegisteredOrThrow(Long userId) {
         if (!userStorage.isUserIdRegistered(userId)) {
             throw new NotFoundException("User id = " + userId + " not found");
         }
    }
}
