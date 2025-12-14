package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private Film validFilm;

    @BeforeEach
    public void setUp() {
        // Инициализируем новый контроллер для каждого теста, чтобы изолировать состояние (Map films)
        filmController = new FilmController();
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
        assertNotNull(films);
        assertEquals(1, films.size());
        assertTrue(films.contains(validFilm));
    }

    @Test
    void create() {
        //Создание валидного объекта
        ResponseEntity<Film> response = filmController.create(validFilm);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Название", response.getBody().getName(), "Название не соответствует ожидаемому");
        assertEquals("Описание", response.getBody().getDescription(), "Описание не соответствует ожидаемому");
        assertEquals(120, response.getBody().getDuration(), "Длительность не соответствует ожидаемому");
        assertEquals("2000-01-01", response.getBody().getReleaseDate().toString(), "Релиза не соответствует ожидаемому");
    }

    @Test
    void update() {
        // Сначала создаем фильм, чтобы получить ID=1
        filmController.create(validFilm);

        // Создаем объект с обновленными данными
        Film updatedFilm = new Film();
        updatedFilm.setId(1L);
        updatedFilm.setName("Обновленное название");
        updatedFilm.setDescription("Обновленное описание");
        updatedFilm.setReleaseDate(LocalDate.of(2011, 1, 1));
        updatedFilm.setDuration(90);

        Film resultFilm = filmController.update(updatedFilm);
        assertEquals(updatedFilm, resultFilm, "Ответ контроллера не соответствует обновленному фильму");
        assertEquals(updatedFilm, filmController.films.get(updatedFilm.getId()), "Значение в контроллере"
                +" не соответствует обновленному фильму");
    }

    @Test
    public void testUpdateFilmWithInvalidId() {
        Film nonExistentFilm = new Film();
        nonExistentFilm.setId(999L);
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> {filmController.update(nonExistentFilm);},
                "Ожидалось исключение " );

        assertEquals("Film id = 999 not found", exception.getMessage());
    }
}