package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {


    Collection<Film> getFilms();

    Film postFilm(Film film);

    Film putFilm(Film film);

    Optional<Film> findById(Long filmId);

}
