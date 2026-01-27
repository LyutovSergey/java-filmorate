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
import java.util.Comparator;
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

        if (!filmStorage.isFilmIdRegistered(newFilm.getId())) {
            throw new NotFoundException("Film id = " + newFilm.getId() + " not found");
        }

        return filmStorage.update(newFilm);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public void likeFilm(Long filmId, Long userId) {
        Film film = getFilmByIdOrThrow(filmId);
        if (userStorage.isUserIdRegistered(userId)) {
            film.addUserLike(userId);
            filmStorage.update(film);
        } else  {
            throw new NotFoundException("User id = " + userId + " not found");
        }

    }

    public void unlikeFilm(Long filmId, Long userId) {
        Film film = getFilmByIdOrThrow(filmId);
        if (userStorage.isUserIdRegistered(userId)) {
            film.removeUserLike(userId);
            filmStorage.update(film);
        } else  {
            throw new NotFoundException("User id = " + userId + " not found");
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count < 1) {
            throw new ConditionsNotMetException("Count must be greater than 0");
        }
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getUserIdLikes().size()).reversed())
                .limit(count)
                .toList();
    }

    private Film getFilmByIdOrThrow(Long filmId) {
        return filmStorage.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Film id = " + filmId + " not found"));
    }
}
