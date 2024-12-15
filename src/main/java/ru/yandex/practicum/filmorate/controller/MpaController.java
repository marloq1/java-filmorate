package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaStorage mpaStorage;

    @GetMapping
    public Collection<MpaDto> getMpa() {
        log.info("Запрос списка рейтингов");
        return mpaStorage.getMpa();
    }

    @GetMapping("/{id}")
    public MpaDto getMpaById(@PathVariable long id) {
        log.info("Запрос рейтинга с id {}",id);
        Optional<MpaDto> mpaDto = mpaStorage.getMpaById(id);
        if (mpaDto.isPresent()) {
            return mpaDto.get();
        } else {
            throw new NotFoundException("Такого рейтинга нет");
        }
    }
}
