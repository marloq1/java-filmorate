package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {


    @Qualifier("UserDb")
    private final UserStorage userStorage;
    @Qualifier("FilmDb")
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;


    public FilmDto getFilm(Long id) {
        FilmDto filmDto = new FilmDto();
        Optional<Film> film = filmStorage.findById(id);
        if (film.isEmpty()){
            throw new NotFoundException("Такого id нет");
        } else {
            filmDto = FilmMapper.mapToFilmDto(film.get());
        }
        return filmDto;
    }

    public List<FilmDto> getFilms() {
        List<FilmDto> films = filmStorage.getFilms().stream().map(FilmMapper::mapToFilmDto).toList();
        return films;
    }

    public FilmDto postFilm(FilmDto filmDto){
        Film film = FilmMapper.mapToFilm(filmDto);
        Set <GenreDto> genreDtoSet = new HashSet<>();
        Optional<MpaDto> mpa;
        for (GenreDto genreDto:film.getGenres()){
            Optional<GenreDto> genre = genreStorage.getGenreById(genreDto.getId());
            if (genre.isEmpty()) {
                throw new ValidationException("Фильм с таким жанром невалиден");
            } else {
                genreDtoSet.add(genre.get());
            }
        }
        if (!genreDtoSet.isEmpty()) {
            film.setGenres(genreDtoSet);
        }
        if (film.getMpa()!=null) {
            mpa = mpaStorage.getMpaById(film.getMpa().getId());
            if (mpa.isEmpty()) {
                throw new ValidationException("Фильм с таким рейтингом невалиден");
            } else {
                film.setMpa(mpa.get());
            }
        }
        return FilmMapper.mapToFilmDto(filmStorage.postFilm(film));
    }

    public FilmDto putFilm(@Valid @RequestBody Film film) {

        return FilmMapper.mapToFilmDto(filmStorage.putFilm(film));

    }

    public FilmDto addLike(Long id, Long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильма с таким id нет"));
        log.trace("id при добавлении валидны");
        return FilmMapper.mapToFilmDto(filmStorage.addLike(id,userId));


    }

    public FilmDto deleteLike(Long id, Long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        Film film = filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильма с таким id нет"));
        log.trace("id при удалении валидны");
        return FilmMapper.mapToFilmDto(filmStorage.deleteLike(id,userId));
    }

    public List<FilmDto> getTopLikedFilms(int count) {
        return new ArrayList<>(filmStorage.getFilms()).stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size()).limit(count)
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }
}
