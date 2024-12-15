package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmDaoRowMapper implements RowMapper<FilmDao> {

    @Override
    public FilmDao mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmDao filmDao = new FilmDao();
        filmDao.setId(rs.getLong("id"));
        filmDao.setName(rs.getString("name"));
        filmDao.setDescription(rs.getString("description"));
        filmDao.setReleaseDate(rs.getDate("release_date").toLocalDate());
        filmDao.setDuration(rs.getInt("duration"));
        filmDao.setMpaId(rs.getLong("mpa_rating_id"));
        filmDao.setGenreId(rs.getLong("genre_id"));
        filmDao.setUserId(rs.getLong("user_id"));
        filmDao.setGenre(rs.getString("genre.name"));
        filmDao.setMpa(rs.getString("mpa_rating.name"));
        return filmDao;
    }
}
