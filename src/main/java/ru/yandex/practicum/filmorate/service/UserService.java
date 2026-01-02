package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public User create (User user) {
        return userStorage.create(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriend(Long userId, Long friendUserId) {
       User user = userStorage.getById(userId)
               .orElseThrow(() -> new NotFoundException("User id = " + userId + " not found"));
       User friendUser = userStorage.getById(friendUserId)
               .orElseThrow(() -> new NotFoundException("User id = " + friendUserId + " not found"));

       user.addFriend(friendUserId);
       friendUser.addFriend(userId);

       userStorage.update(user);
       userStorage.update(friendUser);
    }

    public void removeFriend(Long userId, Long friendUserId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("User id = " + userId + " not found"));
        User friendUser = userStorage.getById(friendUserId)
                .orElseThrow(() -> new NotFoundException("User id = " + friendUserId + " not found"));

        user.removeFriend(friendUserId);
        friendUser.removeFriend(userId);

        userStorage.update(user);
        userStorage.update(friendUser);
    }

    public Collection<User> getFriends(Long userId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("User id = " + userId + " not found"));

        return user.getFriendsId().stream()
                .map(friendId -> userStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException("User friend id = " + userId + " not found")))
                .toList();
    }

    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = userStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException("User id = " + userId + " not found"));
        User otherUser = userStorage.getById(otherUserId)
                .orElseThrow(() -> new NotFoundException("User id = " + userId + " not found"));
        Set<Long> CommonFriendsId = new HashSet<>(user.getFriendsId());
        CommonFriendsId.retainAll(otherUser.getFriendsId());
        return CommonFriendsId.stream()
                .map(friendId -> userStorage.getById(friendId)
                        .orElseThrow(() -> new NotFoundException("User friend id = " + userId + " not found")))
                .toList();
    }
}
