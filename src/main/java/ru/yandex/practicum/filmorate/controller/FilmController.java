package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Запрос коллекции фильмов");
        return films.values();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info("Добавление нового фильма");
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        log.info("Замена фильма");
        if (film.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Не указан id");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;

        } else {
            log.error("Такого id нет в списке фильмов");
            throw new ValidationException("Такого id нет в списке фильмов");
        }

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
