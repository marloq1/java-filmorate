package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.util.Collection;
import java.util.Optional;

@Repository("GenreDb")
public class GenreStorage extends BaseDbStorage<GenreDto> {

    private static final String FIND_ONE_QUERY = "SELECT * FROM GENRE WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM GENRE";

    public GenreStorage(JdbcTemplate jdbcTemplate, RowMapper<GenreDto> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Collection<GenreDto> getGenres() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<GenreDto> getGenreById(long id) {
        return findOne(FIND_ONE_QUERY, id);
    }
}
