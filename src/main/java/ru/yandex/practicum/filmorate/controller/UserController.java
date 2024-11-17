package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Запрос коллекции пользователей");
        return userStorage.getUsers();
    }


    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        log.info("Добавление нового пользователя");
        return userStorage.postUser(user);
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        log.info("Замена пользователя");
        if (user.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Не указан id");
        }
        return userStorage.putUser(user);

    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.info("Запрос пользователя по id");
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable long id) {
        log.info("Запрос друзей пользователя");
        return userService.getUserFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Добавление друга");
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Удаление друга");
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriendsList(@PathVariable long id, @PathVariable long otherId) {
        log.info("Вывод общего списка друзей");
        return userService.getMutualFriendsList(id, otherId);
    }


}

