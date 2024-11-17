package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public Film addLike(Long id, Long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        Film film = filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильма с таким id нет"));
        log.trace("id при добавлении валидны");
        film.getLikes().add(userId);
        log.trace("Лайк успешно добавлен");
        return film;

    }

    public Film deleteLike(Long id, Long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        Film film = filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильма с таким id нет"));
        log.trace("id при удалении валидны");
        film.getLikes().remove(userId);
        log.trace("Лайк успешно удален");
        return film;
    }

    public List<Film> getTopLikedFilms(int count) {
        return new ArrayList<>(filmStorage.getFilms()).stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size()).limit(count)
                .toList();
    }
}
