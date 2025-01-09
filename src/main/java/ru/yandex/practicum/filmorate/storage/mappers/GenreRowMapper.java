package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.GenreDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GenreRowMapper implements RowMapper<GenreDto> {

    @Override
    public GenreDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        GenreDto genreDto = new GenreDto();
        genreDto.setName(resultSet.getString("name"));
        genreDto.setId(resultSet.getLong("id"));
        return genreDto;
    }
}
