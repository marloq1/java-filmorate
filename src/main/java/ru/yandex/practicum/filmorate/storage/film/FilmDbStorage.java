package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exceptions.InternalServerException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository("FilmDb")
public class FilmDbStorage extends BaseDbStorage<FilmDao> implements FilmStorage {


    private static final String FIND_ALL_QUERY = "SELECT * FROM FILMS f " +
            "LEFT JOIN FILM_GENRE fg ON fg.FILM_ID =f.ID " +
            "LEFT JOIN FILM_LIKES fl ON fl.FILM_ID=f.ID " +
            "LEFT JOIN GENRE g ON g.id=fg.GENRE_ID " +
            "LEFT JOIN MPA_RATING mr ON mr.id=f.MPA_RATING_ID ";
    private static final String FIND_ID_QUERY = "SELECT * FROM FILMS f " +
            "LEFT JOIN FILM_GENRE fg ON fg.FILM_ID =f.ID " +
            "LEFT JOIN FILM_LIKES fl ON fl.FILM_ID=f.ID " +
            "LEFT JOIN GENRE g ON g.id=fg.GENRE_ID " +
            "LEFT JOIN MPA_RATING mr ON mr.id=f.MPA_RATING_ID " +
            "WHERE f.id=? ";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date,duration," +
            "mpa_rating_id) VALUES (?, ?, ?, ?, ?) ";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, mpa_rating_id = ? WHERE id = ?";
    private static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?) ";
    private static final String INSERT_FILM_LIKES_QUERY = "INSERT INTO FILM_LIKES(film_id, user_id) VALUES (?, ?) ";
    private static final String DELETE_FILM_LIKES_QUERY = "DELETE FROM FILM_LIKES WHERE film_id=? AND user_id=? ";

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<FilmDao> mapper) {
        super(jdbcTemplate, mapper);

    }

    @Override
    public List<Film> getFilms() {
        return FilmMapper.mapToFilmList(findMany(FIND_ALL_QUERY));
    }

    @Override
    public Film postFilm(Film film) {
        Long mpaId = null;
        if (film.getMpa() != null) {
            mpaId = film.getMpa().getId();
        }
        Long id = insertWithKey(INSERT_QUERY, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), mpaId);
        film.setId(id);
        for (GenreDto genre : film.getGenres()) {
            insert(INSERT_FILM_GENRE_QUERY, film.getId(), genre.getId());
        }
        return findById(id).orElseThrow(() -> new InternalServerException("Фильм недобавлен"));
    }

    @Override
    public Film putFilm(Film film) {
        Long mpaId = null;
        if (film.getMpa() != null) {
            mpaId = film.getMpa().getId();
        }
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                mpaId,
                film.getId()
        );
        return findById(film.getId()).orElseThrow(() -> new NotFoundException("Такого id нет"));
    }

    @Override
    public Optional<Film> findById(Long filmId) {
        return FilmMapper.mapToFilmList(findMany(FIND_ID_QUERY, filmId)).stream().filter(film -> film.getId().equals(filmId)).findAny();
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        insert(INSERT_FILM_LIKES_QUERY, filmId, userId);
        return findById(filmId).orElseThrow(() -> new NotFoundException("Такого id нет"));
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        update(DELETE_FILM_LIKES_QUERY, filmId, userId);
        return findById(filmId).orElseThrow(() -> new NotFoundException("Такого id нет"));
    }
}
