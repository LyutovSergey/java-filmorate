package ru.yandex.practicum.filmorate.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        validUser = new User(null,
                "test@test.ru",
                "логин",
                "Имя",
                LocalDate.of(2000, 1, 1));
    }

    @Test
    void findAll() {
        userController.create(validUser);
        Collection<User> user = userController.findAll();
        assertNotNull(user);
        assertEquals(1, user.size());
        assertTrue(user.contains(validUser));
    }

    @Test
    void create() {
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
        User updatedFilm = new Film();
        updatedFilm.setId(1L);
        updatedFilm.setEmail("test2");
        updatedFilm.setDescription("Обновленное описание");
        updatedFilm.setReleaseDate(LocalDate.of(2011, 1, 1));
        updatedFilm.setDuration(90);

        Film resultFilm = filmController.update(updatedFilm);
        assertEquals(updatedFilm, resultFilm, "Ответ контроллера не соответствует обновленному фильму");
        assertEquals(updatedFilm, filmController.films.get(updatedFilm.getId()), "Значение в контроллере"
                +" не соответствует обновленному фильму");
    }

    @Test
    void isDuplicateEmail() {
    }
}