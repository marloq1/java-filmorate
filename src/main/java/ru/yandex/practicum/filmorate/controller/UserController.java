package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getUsers() {
        log.info("Запрос коллекции пользователей");
        return userService.getUsers();
    }


    @PostMapping
    public UserDto postUser(@Valid @RequestBody UserDto userDto) {
        log.info("Добавление нового пользователя");
        return userService.postUser(userDto);
    }

    @PutMapping
    public UserDto putUser(@Valid @RequestBody UserDto userDto) {
        log.info("Замена пользователя");
        if (userDto.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Не указан id");
        }
        return userService.putUser(userDto);

    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {
        log.info("Запрос пользователя по id");
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<UserDto> getUserFriends(@PathVariable long id) {
        log.info("Запрос друзей пользователя");
        return userService.getUserFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public UserDto addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Добавление друга");
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public UserDto deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Удаление друга");
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserDto> getMutualFriendsList(@PathVariable long id, @PathVariable long otherId) {
        log.info("Вывод общего списка друзей");
        return userService.getMutualFriendsList(id, otherId);
    }


}

