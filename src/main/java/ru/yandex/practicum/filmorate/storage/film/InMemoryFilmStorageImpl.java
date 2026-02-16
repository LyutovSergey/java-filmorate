package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.common.IdGenerator;

import java.util.*;

@Repository("inMemoryFilmStorageImpl")
public class InMemoryFilmStorageImpl implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final IdGenerator idGenerator;

    public InMemoryFilmStorageImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public Film create(Film film) {
        Film newFilm = film.copy(); // решил делать двойной "build"
        newFilm.setId(idGenerator.getNextId());
        films.put(newFilm.getId(), newFilm);
        return newFilm.copy();
    }

    @Override
    public Film update(Film film) {
            Film newFilm = film.copy();
            films.put(newFilm.getId(), newFilm);
            return newFilm.copy();
  }

    @Override
    public Collection<Film> findAll() {
        return films.values().stream()
                .map(Film::copy)
                .toList();
    }

    @Override
    public Optional<Film> getById(Long id) {
        return Optional.ofNullable(films.get(id))
                .map(Film::copy);
    }

    @Override
    public boolean isFilmIdRegistered(Long filmId) {
        return films.containsKey(filmId);
    }

    @Override
    public void likeFilm(Long filmId, Long userId) {
        Film film = films.get(filmId);
        film.getUserIdLikes().add(userId);
    }

    @Override
    public void unlikeFilm(Long filmId, Long userId) {
        Film film = films.get(filmId);
        if (!film.getUserIdLikes().remove(userId)) {
            throw new NotFoundException("User id = " + userId + " like film id = " + filmId + " not found");
        }
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getUserIdLikes().size()).reversed())
                .limit(count)
                .toList();
    }
}
