package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Запрос коллекции фильмов");
        return filmStorage.getFilms();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info("Добавление нового фильма");
        return filmStorage.postFilm(film);
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        log.info("Замена фильма");
        if (film.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Не указан id");
        }
        return filmStorage.putFilm(film);

    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        log.info("Запрос фильма по id");
        return filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addFriend(@PathVariable long id, @PathVariable long userId) {
        log.info("Добавление лайка");
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteFriend(@PathVariable long id, @PathVariable long userId) {
        log.info("Удаление лайка");
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopLikedFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Вывод самых популярных фильмов по лайкам");
        return filmService.getTopLikedFilms(count);
    }

}
