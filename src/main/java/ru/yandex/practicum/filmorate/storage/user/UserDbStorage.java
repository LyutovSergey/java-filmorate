package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


    @Slf4j
    @Repository
    public class UserDbStorage implements UserStorage {
        @Override
        public User create(User user) {
            return null;
        }

        @Override
        public User update(User user) {
            return null;
        }

        @Override
        public Optional<User> getById(Long id) {
            return Optional.empty();
        }

        @Override
        public Collection<User> findAll() {
            return List.of();
        }

        @Override
        public boolean isEmailRegistered(String email) {
            return false;
        }

        @Override
        public boolean isUserIdRegistered(Long userId) {
            return false;
        }
    }

