package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;

    public FilmController(@Autowired FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping
    public Collection<Film> findAll() {
        //log.info("Get list films");
        //log.debug("{}", films.values());
        return filmService.findAll() ;
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        log.info("Film creation request: {}", film.toString());
        Film newFilm = filmService.create(film);
        return new ResponseEntity<>(newFilm, HttpStatus.CREATED);
    }

    // вспомогательный метод для генерации идентификатора нового пользователя


    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Film update request: {}", newFilm.toString());
        return filmService.update(newFilm);
    }
}
