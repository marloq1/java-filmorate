package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;

@Slf4j
@Component("UserInMem")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return users.values().stream().toList();
    }

    @Override
    public User postUser(User user) {

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User putUser(User user) {

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь изменен");
            return user;

        } else {
            log.error("Такого id нет в списке фильмов");
            throw new NotFoundException("Такого id нет в списке фильмов");
        }
    }

    @Override
    public Optional<User> findById(Long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        } else
            return Optional.empty();
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public void addFriend(Long userId, Long friendId, boolean status) {
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
    }
}
