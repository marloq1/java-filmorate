package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.MpaDto;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaStorage extends BaseDbStorage<MpaDto> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM MPA_RATING";
    private static final String FIND_ONE_QUERY = "SELECT * FROM MPA_RATING WHERE id = ?";

    public MpaStorage(JdbcTemplate jdbcTemplate, RowMapper<MpaDto> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Collection<MpaDto> getMpa() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<MpaDto> getMpaById(long id) {
        return findOne(FIND_ONE_QUERY, id);
    }
}
