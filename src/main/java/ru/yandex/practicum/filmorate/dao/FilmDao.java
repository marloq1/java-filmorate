package ru.yandex.practicum.filmorate.dao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
