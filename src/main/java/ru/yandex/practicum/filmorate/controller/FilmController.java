package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    @Qualifier("FilmDb")
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @GetMapping
    public Collection<FilmDto> getFilms() {
        log.info("Запрос коллекции фильмов");
        return filmService.getFilms();
    }

    @PostMapping
    public FilmDto postFilm(@Valid @RequestBody FilmDto film) {
        log.info("Добавление нового фильма");

       return filmService.postFilm(film);
    }

    @PutMapping
    public FilmDto putFilm(@Valid @RequestBody Film film) {
        log.info("Замена фильма");
        if (film.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Не указан id");
        }
        return filmService.putFilm(film);

    }

    @GetMapping("/{id}")
    public FilmDto getFilm(@PathVariable long id) {
        log.info("Запрос фильма по id");
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmDto addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Добавление лайка");
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmDto deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Удаление лайка");
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getTopLikedFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Вывод самых популярных фильмов по лайкам");
        return filmService.getTopLikedFilms(count);
    }

}
