package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<GenreDto> genres = new TreeSet<>((gd1, gd2) -> (int) (gd1.getId() - gd2.getId()));
    private MpaDto mpa;
    private Set<Long> likes = new HashSet<>();
}




