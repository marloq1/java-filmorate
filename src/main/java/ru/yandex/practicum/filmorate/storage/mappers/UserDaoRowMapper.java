package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDaoRowMapper implements RowMapper<UserDao> {

    @Override
    public UserDao mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        UserDao user = new UserDao();
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setBirthday(resultSet.getDate("birthday").toLocalDate());
        user.setReceiverId(resultSet.getLong("receiver_id"));
        user.setApproved(resultSet.getBoolean("is_approved"));
        return user;
    }
}