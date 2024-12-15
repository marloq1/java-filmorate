package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

   private final GenreStorage genreStorage;

    @GetMapping
    public Collection<GenreDto> getGenre() {
        log.info("Запрос жанров");
        return genreStorage.getGenres();
    }

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable long id) {
        log.info("Запрос жанра с id {}",id);
        Optional<GenreDto> genreDto = genreStorage.getGenreById(id);
        if (genreDto.isPresent()){
            return genreDto.get();
        } else {
            throw  new NotFoundException("Жанра с таким id нет");
        }
    }
}
