package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.common.IdGenerator;

import java.util.*;

@Slf4j
@Repository("inMemoryUserStorageImpl")
public class
InMemoryUserStorageImpl implements UserStorage {

    protected final Map<Long, User> users = new HashMap<>();
    private final IdGenerator idGenerator;

    public InMemoryUserStorageImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public User create(User user) {

        User newUser = user.copy(); // решил заменить build на внутренний метод чтобы копировать коллекцию
        newUser.calculateUserName();
        newUser.setId(idGenerator.getNextId());
        users.put(newUser.getId(), newUser);
        log.info("User created!");
        return newUser.copy();
    }

    @Override
    public User update(User user) {
            User newUser = user.copy();
            newUser.calculateUserName();
            users.put(newUser.getId(), newUser);
            log.info("User updated!");
            return newUser.copy();
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.ofNullable(users.get(id))
                .map(User::copy);
    }

    @Override
    public Collection<User> findAll() {
        log.debug("{}", users.values());
        return users.values().stream()
                .map(User::copy)
                .toList();
    }

    @Override
    public boolean isEmailRegistered(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().orElse(null) != null;
    }

    @Override
    public boolean isUserIdRegistered(Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public void removeFriend(Long userId, Long friendUserId) {
        User user = users.get(userId);
        if (!user.getFriendsId().remove(friendUserId)) {
            throw new NotFoundException("User id = " + userId + " not have friend id = " + friendUserId);
        }

    }

    @Override
    public void addFriend(Long userId, Long friendUserId) {
        users.get(userId).getFriendsId().add(friendUserId);

    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = users.get(userId);
        User otherUser = users.get(otherUserId);
        Set<Long> commonFriendsId = new HashSet<>(user.getFriendsId());

        commonFriendsId.retainAll(otherUser.getFriendsId());
        return commonFriendsId.stream()
                .map(users::get)
                .map(User::copy)
                .toList();
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return users.get(userId).getFriendsId().stream()
                .map(users::get)
                .map(User::copy)
                .toList();
    }
}
