package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class UserMapper {

    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setLogin(userDto.getLogin());
        user.setName(userDto.getName());
        user.setBirthday(userDto.getBirthday());
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setName(user.getName());
        userDto.setBirthday(user.getBirthday());
        for (Long friendId : user.getFriends()) {
            userDto.getFriends().add(friendId);
        }
        return userDto;
    }

    public static List<User> mapToUserList(List<UserDao> usersDao) {
        Map<Long, User> users = new HashMap<>();
        if (usersDao.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Long bufId = usersDao.getFirst().getId();
        int indexUsD;

        User user = mapDaoToUser(usersDao.getFirst());
        users.put(bufId, user);

        for (int i = 0; i < usersDao.size(); i++) {
            if (!(bufId.equals(usersDao.get(i).getId()))) {
                indexUsD = i;
                bufId = usersDao.get(indexUsD).getId();
                users.put(bufId, mapDaoToUser(usersDao.get(indexUsD)));

            }
            if (usersDao.get(i).getReceiverId() != 0) {
                users.get(bufId).getFriends().add(usersDao.get(i).getReceiverId());
            }
        }
        for (int i = 0; i < usersDao.size(); i++) {
            if (usersDao.get(i).isApproved() && users.get(usersDao.get(i).getReceiverId()) != null) {
                users.get(usersDao.get(i).getReceiverId()).getFriends().add(usersDao.get(i).getId());
            }
        }


        return users.values().stream().toList();
    }

    private static User mapDaoToUser(UserDao userDao) {
        User user = new User();
        user.setName(userDao.getName());
        user.setEmail(userDao.getEmail());
        user.setBirthday(userDao.getBirthday());
        user.setLogin(userDao.getLogin());
        user.setId(userDao.getId());
        return user;
    }
}
