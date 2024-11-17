package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User postUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User putUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
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

}
