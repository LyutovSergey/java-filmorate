package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

@Repository
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
}
