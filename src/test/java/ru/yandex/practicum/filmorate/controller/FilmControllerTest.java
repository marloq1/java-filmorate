package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    static Film film;
    static FilmController filmController = new FilmController();

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
        assertThrows(ValidationException.class, () -> filmController.putFilm(film));
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