package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    User update(User user);

    Optional<User> getById(Long id);

    Collection<User> findAll();

    boolean isEmailRegistered(String email);

    boolean isUserIdRegistered(Long userId);

    void removeFriend(Long userId, Long friendUserId);

    void addFriend(Long userId, Long friendUserId);

    Collection<User> getCommonFriends(Long userId, Long otherUserId);

    Collection<User> getFriends(Long userId);
}

