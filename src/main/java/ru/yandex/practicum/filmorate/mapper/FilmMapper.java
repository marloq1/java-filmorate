package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;

public class FilmMapper {

    public static Film mapToFilm(FilmDto filmDto){
        Film film = new Film();
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDuration(filmDto.getDuration());

        for (GenreDto genre:  filmDto.getGenres()) {
            film.getGenres().add(genre);
        }
        if (filmDto.getMpa()!=null)
        film.setMpa(filmDto.getMpa());
        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        for (GenreDto genre:  film.getGenres()){
            filmDto.getGenres().add(genre);
        }
        filmDto.setMpa(film.getMpa());
        filmDto.setLikes(new HashSet<>(film.getLikes()));
        return filmDto;
    }
    public static List<Film> mapToFilmList(List<FilmDao> filmsDao) {
        Map<Long,Film> films = new HashMap<>();
        if (filmsDao.isEmpty()){
            return Collections.EMPTY_LIST;
        }
        Long bufId = filmsDao.getFirst().getId();
        int indexUsD;

        Film film = mapFilmDaoToFilm(filmsDao.getFirst());
        films.put(bufId,film);

       for (int i=0;i<filmsDao.size();i++) {
            if (!(bufId.equals(filmsDao.get(i).getId()))) {
                indexUsD = i;
                bufId=filmsDao.get(indexUsD).getId();
                films.put(bufId,mapFilmDaoToFilm(filmsDao.get(indexUsD)));

            }
            if (filmsDao.get(i).getGenreId()!=0) {
                GenreDto genreDto =new GenreDto();
                genreDto.setId(filmsDao.get(i).getGenreId());
                genreDto.setName(filmsDao.get(i).getGenre());
                films.get(bufId).getGenres().add(genreDto);
            }
           if (filmsDao.get(i).getUserId()!=0) {
               films.get(bufId).getLikes().add(filmsDao.get(i).getUserId());
           }
        }

        return films.values().stream().toList();
    }

    private static Film mapFilmDaoToFilm(FilmDao filmDao) {
        Film film = new Film();
        film.setId(filmDao.getId());
        film.setName(filmDao.getName());
        film.setDescription(filmDao.getDescription());
        film.setReleaseDate(filmDao.getReleaseDate());
        film.setDuration(filmDao.getDuration());
        if (filmDao.getMpaId()!=0) {
            MpaDto mpaDto = new MpaDto();
            mpaDto.setId(filmDao.getMpaId());
            mpaDto.setName(filmDao.getMpa());
            film.setMpa(mpaDto);
        }
        return film;

    }
}
