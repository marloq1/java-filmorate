package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    static Film film;
    static FilmStorage filmStorage = new InMemoryFilmStorage();
    static UserStorage userStorage = new InMemoryUserStorage();
    static FilmService filmService = new FilmService(userStorage,filmStorage);
    static FilmController filmController = new FilmController(filmStorage,filmService);

    @BeforeEach
    public void addValidFilmTest() {
        film = new Film();
        film.setName("1");
        film.setDuration(60);
        film.setDescription("a".repeat(100));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        assertEquals(filmController.postFilm(film), film);
    }

    @Test
    public void replaceInvalidIdFilmTest() {
        film = new Film();
        film.setName("1");
        film.setDuration(40);
        film.setDescription("a".repeat(100));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setId(3L);
        assertThrows(NotFoundException.class, () -> filmController.putFilm(film));
    }

    @Test
    public void replaceNoIdFilmTest() {
        film = new Film();
        film.setName("1");
        film.setDuration(40);
        film.setDescription("a".repeat(100));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> filmController.putFilm(film));
    }

    @Test
    void replaceValidFilmTest() {
        film = new Film();
        film.setName("1");
        film.setDuration(40);
        film.setDescription("a".repeat(100));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setId(1L);
        assertEquals(filmController.putFilm(film), film);
    }


}