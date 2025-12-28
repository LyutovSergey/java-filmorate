package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.Collection;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(@Autowired FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film update( Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public Film create (Film film) {
        return filmStorage.create(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }
}
