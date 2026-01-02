package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film update( Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public Film create (Film film) {
        return filmStorage.create(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public void likeFilm(Long filmId, Long userId) {
        Film film = getFilmAndValidateUser(filmId, userId);
        film.addUserLike(userId);
        filmStorage.update(film);
    }

    public void unlikeFilm(Long filmId, Long userId) {
        Film film = getFilmAndValidateUser(filmId, userId);
        film.removeUserLike(userId);
        filmStorage.update(film);
    }

    private Film getFilmAndValidateUser(Long filmId, Long userId) {
        if (userStorage.getById(userId).isEmpty()) {
            throw new NotFoundException("User id = " + userId + " not found");
        }
        return filmStorage.getById(filmId)
                .orElseThrow(() -> new NotFoundException("Film id = " + filmId + " not found"));
    }

        public List<Film> getPopularFilms(Integer count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getUserIdLikes().size()).reversed())
                .limit(count)
                .toList();
    }
}
