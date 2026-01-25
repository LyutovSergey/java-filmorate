package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Optional;
public interface UserStorage {

    User create(User user);

    User update(User user);

    Optional<User> getById(Long id);

    Collection<User> findAll();

    // Методы в хранилище для оптимизации выполнения программы
    boolean isEmailRegistered(String email);

    boolean isUserIdRegistered(Long userId);
}

