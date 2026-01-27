package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.common.IdGenerator;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorageImpl;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorageImpl;

import java.time.LocalDate;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

/*
 *  Тестирование полей, валидируемых с помощью validation вроде делать пока не нужно
 */

class FilmControllerTest {

    private FilmController filmController;
    private Film validFilm;


    @BeforeEach
    public void setUp() {
        // Инициализируем новый контроллер для каждого теста, чтобы изолировать состояние (Map films)
        filmController = new FilmController(new FilmService(new InMemoryFilmStorageImpl(new IdGenerator()),
                new InMemoryUserStorageImpl(new IdGenerator())));
        validFilm = new Film();
        validFilm.setName("Название");
        validFilm.setDescription("Описание");
        validFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        validFilm.setDuration(120);
    }

    @Test
    void findAll() {
        filmController.create(validFilm);
        Collection<Film> films = filmController.findAll();
        assertNotNull(films, "Ошибка при создании фильма");
        assertEquals(1, films.size(),"Ошибка при создании фильма");
        validFilm.setId(1L);
        assertTrue(films.contains(validFilm),"Ошибка при создании фильма");
    }

    @Test
    void create() {
        //Создание валидного объекта
        Film  response = filmController.create(validFilm);
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Название", response.getName(), "Название не соответствует ожидаемому");
        assertEquals("Описание", response.getDescription(), "Описание не соответствует ожидаемому");
        assertEquals(120, response.getDuration(), "Длительность не соответствует ожидаемому");
        assertEquals("2000-01-01", response.getReleaseDate().toString(), "Релиза не соответствует ожидаемому");
    }

    @Test
    void update() {
        filmController.create(validFilm);
        Film updatedFilm = new Film();
        updatedFilm.setId(1L);
        updatedFilm.setName("Обновленное название");
        updatedFilm.setDescription("Обновленное описание");
        updatedFilm.setReleaseDate(LocalDate.of(2011, 1, 1));
        updatedFilm.setDuration(90);

        Film resultFilm = filmController.update(updatedFilm);
        assertEquals(updatedFilm, resultFilm, "Ответ контроллера не соответствует обновленному фильму");

    }

    @Test
    public void testUpdateFilmWithInvalidId() {
        Film nonExistentFilm = new Film();
        nonExistentFilm.setId(999L);
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> {
            filmController.update(nonExistentFilm);
            },
                "Ожидалось исключение ");
        assertEquals("Film id = 999 not found", exception.getMessage());
    }
}