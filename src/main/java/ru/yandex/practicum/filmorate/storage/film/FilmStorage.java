package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {


    List<Film> getFilms();

    Film postFilm(Film film);

    Film putFilm(Film film);

    Optional<Film> findById(Long filmId);

    Film addLike(Long filmId, Long userId);

    Film deleteLike(Long filmId, Long userId);

}
