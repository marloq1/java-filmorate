package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> getUsers();

    User postUser(User user);

    User putUser(User user);

    Optional<User> findById(Long userId);

    void addFriend(Long userId, Long friendId,boolean status);

    void deleteFriend(Long userId, Long friendId);
}
