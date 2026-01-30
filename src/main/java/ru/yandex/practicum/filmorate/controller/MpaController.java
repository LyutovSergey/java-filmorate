package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    public MpaController(@Autowired MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Mpa> findAll() {
        log.info("Get list genres");
        return mpaService.findAll();
    }

    @GetMapping("/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa getById(@PathVariable Integer mpaId) {
        log.info("Get genre by id = {}", mpaId);
        return mpaService.getGenreByIdOrThrow(mpaId);
    }
}
