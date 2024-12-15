package ru.yandex.practicum.filmorate.model.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film {

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




