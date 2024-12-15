package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return films.values().stream().toList();
    }

    @Override
    public Film postFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    @Override
    public Film putFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;

        } else {
            log.error("Такого id нет в списке фильмов");
            throw new NotFoundException("Такого id нет в списке фильмов");
        }
    }

    @Override
    public Optional<Film> findById(Long filmId) {
        if (films.containsKey(filmId)) {
            return Optional.of(films.get(filmId));
        }
        return Optional.empty();
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        films.get(filmId).getLikes().add(userId);
        return films.get(filmId);
    }

    @Override
    public Film deleteLike(Long filmId,Long userId){
        films.get(filmId).getLikes().remove(userId);
        return films.get(filmId);
    }
}
