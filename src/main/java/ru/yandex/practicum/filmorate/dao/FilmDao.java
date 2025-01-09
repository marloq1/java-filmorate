package ru.yandex.practicum.filmorate.dao;

import lombok.Data;

import java.time.LocalDate;


@Data
public class FilmDao {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Long mpaId;
    private Long genreId;
    private Long userId;
    private String genre;
    private String mpa;
}
