package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.info("User update request failed! Id is null");
            throw new ConditionsNotMetException("Id must be specified");
        }

        Long userId = newUser.getId();

        User oldUser = userStorage.getById(userId)
                .orElseThrow(() -> {
                    log.info("User update failed! Id={} not found", userId);
                    return new NotFoundException("User id = " + userId + " not found");
                });

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
       User user = getUserByIdOrThrow(userId);
       User friendUser = getUserByIdOrThrow(friendUserId);

       user.addFriend(friendUserId);
       friendUser.addFriend(userId);

       userStorage.update(user);
       userStorage.update(friendUser);
    }

    public void removeFriend(Long userId, Long friendUserId) {
        User user = getUserByIdOrThrow(userId);
        User friendUser = getUserByIdOrThrow(friendUserId);

        user.removeFriend(friendUserId);
        friendUser.removeFriend(userId);

        userStorage.update(user);
        userStorage.update(friendUser);
    }

    public Collection<User> getFriends(Long userId) {
        User user = getUserByIdOrThrow(userId);
        return user.getFriendsId().stream()
                .map(friendId -> getUserByIdOrThrow(friendId, "User friend id = " + friendId + " not found"))
                .toList();
    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = getUserByIdOrThrow(userId);
        User otherUser = getUserByIdOrThrow(otherUserId);
        Set<Long> commonFriendsId = new HashSet<>(user.getFriendsId());

        commonFriendsId.retainAll(otherUser.getFriendsId());
        return commonFriendsId.stream()
                .map(friendId -> getUserByIdOrThrow(friendId, "User friend id = " + friendId + " not found"))
                .toList();
    }

    private User getUserByIdOrThrow(Long userId) {
        return userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("User id = " + userId + " not found"));
    }

    private User getUserByIdOrThrow(Long userId, String errorMessage) {
        return userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(errorMessage));
    }
}
