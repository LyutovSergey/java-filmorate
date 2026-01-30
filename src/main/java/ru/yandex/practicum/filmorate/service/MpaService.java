package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class MpaService {
    @Autowired
    private final MpaStorage mpaStorage;

    public Collection<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    public Mpa getGenreByIdOrThrow(int id) {
        return mpaStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Genre id = " + id + " not found"));
    }

}
