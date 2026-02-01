package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(
            @Qualifier("filmDbStorage") FilmStorage filmStorage,
            @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ConditionsNotMetException("Id must be specified");
        }
        filmIdRegisteredOrThrow(newFilm.getId());
        return filmStorage.update(newFilm);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public void likeFilm(Long filmId, Long userId) {
        filmIdRegisteredOrThrow(filmId);
        if (userStorage.isUserIdRegistered(userId)) {
            filmStorage.likeFilm(filmId, userId);
        } else {
            throw new NotFoundException("User id = " + userId + " not found");
        }

    }

    public void unlikeFilm(Long filmId, Long userId) {
        filmIdRegisteredOrThrow(filmId);
        if (userStorage.isUserIdRegistered(userId)) {
            filmStorage.unlikeFilm(filmId, userId);
        } else {
            throw new NotFoundException("User id = " + userId + " not found");
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count < 1) {
            throw new ConditionsNotMetException("Count must be greater than 0");
        }
        return filmStorage.getPopularFilms(count);
    }

    public Film getFilmByIdOrThrow(Long filmId) {
        return filmStorage.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Film id = " + filmId + " not found"));
    }

    private void filmIdRegisteredOrThrow(Long filmId) {
        if (!filmStorage.isFilmIdRegistered(filmId)) {
            throw new NotFoundException("Film id = " + filmId + " not found");
        }
    }
}
