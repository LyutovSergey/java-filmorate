package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {
    @Autowired
    private final GenreStorage genreStorage;

    public Collection<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre getGenreByIdOrThrow(int id) {
        return genreStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Genre id = " + id + " not found"));
    }

}
