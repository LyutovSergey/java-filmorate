package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class InMemoryFilmStorageImpl implements FilmStorage {

    protected final Map<Long, Film> films = new HashMap<>();


    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film.toBuilder().build();
    }

    @Override
    public Film update(Film newFilm ){
        if (newFilm.getId() == null) {
            throw new ConditionsNotMetException("Id must be specified");
        }
        if (films.containsKey(newFilm.getId())) {
            films.put(newFilm.getId(), newFilm);
            return newFilm.toBuilder().build();
        }
        throw new NotFoundException("Film id = " + newFilm.getId() + " not found");
    }

    @Override
    public Collection<Film> findAll() {

        return films.values();

    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
