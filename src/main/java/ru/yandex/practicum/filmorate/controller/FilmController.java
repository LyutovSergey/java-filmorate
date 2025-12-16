package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    protected final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Get list films");
        log.debug("{}", films.values());
        return films.values();
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        log.info("Film creation request: {}", film.toString());
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Film created");
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Film update request: {}", newFilm.toString());
        if (newFilm.getId() == null) {
            log.info("Film update request failed! Id is null");
            throw new ConditionsNotMetException("Id must be specified");
        }
        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            log.info("Film updated!");
            return newFilm;
        }
        log.info("Film update request failed! id = {} not found", newFilm.getId());
        throw new NotFoundException("Film id = " + newFilm.getId() + " not found");
    }
}
