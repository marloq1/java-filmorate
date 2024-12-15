package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository("UserDb")
public class UserDbStorage extends BaseDbStorage<UserDao> implements UserStorage{

    private static final String FIND_ALL_QUERY = "SELECT * FROM users AS u LEFT JOIN friends AS f ON u.id=f.sender_id";
    private static final String FIND_ID_QUERY = "SELECT * FROM users AS u LEFT JOIN friends AS f ON u.id=f.sender_id " +
            "WHERE u.id=? OR f.receiver_id=?";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name,birthday)" +
            "VALUES (?, ?, ?, ?) ";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    private static final String FIND_FRIENDS_QUERY = "SELECT * FROM users AS u LEFT JOIN friends AS f " +
            "ON u.id=f.sender_id WHERE (id = ? OR RECEIVER_ID =?) AND (id = ? OR RECEIVER_ID =?)";
    private static final String INSERT_QUERY_FRIENDS = "INSERT INTO friends(sender_id, receiver_id, is_approved)" +
            "VALUES (?, ?, ?) ";
    private static final String UPDATE_QUERY_FRIENDS = "UPDATE friends SET is_approved = ? WHERE sender_id = ? " +
            "AND receiver_id = ?";
    private static final String UPDATE_SWAP_FRIENDS = "UPDATE friends SET sender_id = ?, receiver_id = ?," +
            "is_approved = ? WHERE sender_id = ? AND receiver_id = ?";
    private static final String DELETE_QUERY_FRIENDS = "DELETE FROM friends WHERE sender_id=? " +
            "AND receiver_id = ?";

    public UserDbStorage(JdbcTemplate jdbcTemplate, RowMapper<UserDao> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<User> getUsers() {
        return UserMapper.mapToUserList(findMany(FIND_ALL_QUERY));
    }

    @Override
    public User postUser(User user) {
        Long id = insertWithKey(INSERT_QUERY,user.getEmail(),user.getLogin(),user.getName(), Date.valueOf(user.getBirthday()));
        user.setId(id);
        return user;
    }

    @Override
    public User putUser(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
       return UserMapper.mapToUserList(findMany(FIND_ID_QUERY,userId,userId)).stream().filter(user -> user.getId().equals(userId)).findAny();
    }

    @Override
    public void addFriend(Long user_id, Long friendId,boolean status) {
        if (!status) {
            insert(INSERT_QUERY_FRIENDS, user_id, friendId, status);
        } else {
            update(UPDATE_QUERY_FRIENDS,status,friendId,user_id);
        }
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        if (findOne(FIND_FRIENDS_QUERY,userId,userId,friendId,friendId).isPresent()) {
            UserDao userDao = findOne(FIND_FRIENDS_QUERY,userId,userId,friendId,friendId).get();
            if (!userDao.isApproved()) {
                if (findOne(FIND_FRIENDS_QUERY, userId, 0, 0, friendId).isPresent()) {
                    update(DELETE_QUERY_FRIENDS, userId, friendId);
                }
            } else {
                if (findOne(FIND_FRIENDS_QUERY, userId, 0, 0, friendId).isPresent()) {
                    update(UPDATE_SWAP_FRIENDS, friendId,userId,  false, userId,friendId );
                } else {
                    update(UPDATE_QUERY_FRIENDS, false, friendId,userId);
                }
            }
        }

    }

}
