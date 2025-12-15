package ru.yandex.practicum.filmorate.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/*
 *  Тестирование полей, валидируемых с помощью validation вроде делать пока не нужно
 */

class UserControllerTest {
    private UserController userController;
    private User validUser;

    @BeforeEach
    public void setUp() {
        // Инициализируем новый контроллер для каждого теста, чтобы изолировать состояние (Map films)
        userController = new UserController();
        validUser = new User();
        validUser.setEmail("test@test.ru");
        validUser.setLogin("логин");
        validUser.setBirthday(LocalDate.of(2000, 1, 1));
        validUser.setName("Имя");
    }

    @Test
    void findAll() {
        userController.create(validUser);
        Collection<User> users = userController.findAll();
        assertNotNull(users, "Ошибка при создании пользователя");
        assertEquals(1, users.size(), "Ошибка при создании пользователя");
        assertTrue(users.contains(validUser), "Ошибка при создании пользователя");
    }

    @Test
    void create() {
        //Создание валидного объекта
        ResponseEntity<User> response = userController.create(validUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("test@test.ru", response.getBody().getEmail(), "Email не соответствует ожидаемому");
        assertEquals("логин", response.getBody().getLogin(), "Login не соответствует ожидаемому");
        assertEquals("Имя", response.getBody().getName(), "Name не соответствует ожидаемому");
        assertEquals("2000-01-01", response.getBody().getBirthday().toString(), "Birthday не соответствует ожидаемому");
    }

    @Test
    void update() {
        userController.create(validUser);
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Обновленное Имя");
        updatedUser.setEmail("test2@test.ru");
        updatedUser.setBirthday(LocalDate.of(2011, 1, 1));
        updatedUser.setLogin("логин2");

        User resultUser = userController.update(updatedUser);
        assertEquals(updatedUser, resultUser, "Ответ контроллера не соответствует обновленному пользователю");
        assertEquals(updatedUser, userController.users.get(updatedUser.getId()), "Значение в контроллере"
                +" не соответствует обновленному пользователю");
    }

    @Test
    public void testUpdateFilmWithInvalidId() {
        User nonExistentUser = new User();
        nonExistentUser.setId(999L);
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> {userController.update(nonExistentUser);},
                "Ожидалось исключение " );

        assertEquals("User id = 999 not found", exception.getMessage());
    }

    @Test
    void testAddUserWithDuplicateEmail() {
        userController.create(validUser);
        DuplicatedDataException exception = assertThrows(DuplicatedDataException.class,
                () -> {
                    userController.create(validUser);
                    },
                "Ожидалось исключение");
        assertEquals("This email is already in use", exception.getMessage());
    }
}