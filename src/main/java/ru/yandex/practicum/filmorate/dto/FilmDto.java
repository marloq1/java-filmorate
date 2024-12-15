package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import java.time.LocalDate;
import java.util.*;

@Data
public class FilmDto {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @MinimumDate(value = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<GenreDto> genres = new TreeSet<>((gd1, gd2) -> (int) (gd1.getId() - gd2.getId()));
    private MpaDto mpa;
    private Set<Long> likes = new HashSet<>();
}
