package ru.yandex.practicum.filmorate.storage;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Repository
public class InMemoryUserStorageImpl implements UserStorage {

    protected final Map<Long, User> users = new HashMap<>();
    private final IdGenerator idGenerator;

    public InMemoryUserStorageImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public User create(User user) {
        if (isDuplicateEmail(user.getEmail())) {
            log.info("User creation failed! email={} is already in use", user.getEmail());
            throw new DuplicatedDataException("This email is already in use");
        }
        User newUser = user.toBuilder().build();
        newUser.calculateUserName();
        newUser.setId(idGenerator.getNextId());
        users.put(newUser.getId(), newUser);
        log.info("User created!");
        return newUser.toBuilder().build();
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            log.info("User update request failed! Id is null");
            throw new ConditionsNotMetException("Id must be specified");
        }

        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            if (!oldUser.getEmail().equals(user.getEmail())) {
                if (isDuplicateEmail(user.getEmail())) {
                    log.info("User update request failed! email={} is already in use", user.getEmail());
                    throw new DuplicatedDataException("This email is already in use");
                }
            }
            User newUser= user.toBuilder().build();
            newUser.calculateUserName();
            users.put(newUser.getId(), newUser);
            log.info("User updated!");
            return newUser.toBuilder().build();
        }
        log.info("User update request failed! id={} not found", user.getId());
        throw new NotFoundException("User id = " + user.getId() + " not found");

    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.ofNullable(users.get(id))
                .map(f -> f.toBuilder().build());
    }

    @Override
    public Collection<User> findAll() {
        log.debug("{}", users.values());
        return users.values().stream()
                .map(user -> user.toBuilder().build())
                .toList();
    }

    boolean isDuplicateEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst().orElse(null) != null;
    }

}
