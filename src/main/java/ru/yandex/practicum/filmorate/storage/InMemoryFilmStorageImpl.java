package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        Film newFilm = film.toBuilder().build();
        newFilm.setId(idGenerator.getNextId());
        films.put(newFilm.getId(), newFilm);
        return newFilm.toBuilder().build();
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new ConditionsNotMetException("Id must be specified");
        }
        if (films.containsKey(film.getId())) {
            Film newFilm = film.toBuilder().build();
            films.put(newFilm.getId(), newFilm);
            return newFilm.toBuilder().build();
        }
        throw new NotFoundException("Film id = " + film.getId() + " not found");
    }

    @Override
    public Collection<Film> findAll() {
        return films.values().stream()
                .map(film -> film.toBuilder().build())
                .toList();
    }

    @Override
    public Optional<Film> getById(Long id) {
        return Optional.ofNullable(films.get(id))
                .map(f -> f.toBuilder().build());
    }


}
