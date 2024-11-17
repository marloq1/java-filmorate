package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User addFriend(Long id, Long friendId) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
        User friendUser = userStorage.findById(friendId).orElseThrow(() -> new NotFoundException("Такого id нет"));
        log.trace("id при добавлении валидны");
        user.getFriends().add(friendId);
        friendUser.getFriends().add(id);
        log.trace("Друг успешно добавлен");
        return user;
    }

    public User deleteFriend(Long id, Long friendId) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
        User friendUser = userStorage.findById(friendId).orElseThrow(() -> new NotFoundException("Такого id нет"));
        log.trace("id при удалении валидны");
        user.getFriends().remove(friendId);
        friendUser.getFriends().remove(id);
        log.trace("Друг успешно удален");
        return user;
    }

    public List<User> getUserFriends(Long id) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
        log.trace("id при получении списка друзей валиден");
        return new ArrayList<>(user.getFriends().stream()
                .map((id1) -> userStorage.findById(id1).orElseThrow(() -> new NotFoundException("Такого id нет")))
                .toList());
    }

    public List<User> getMutualFriendsList(Long id, Long otherId) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
        User otherUser = userStorage.findById(otherId).orElseThrow(() -> new NotFoundException("Такого id нет"));
        log.trace("id при получении общих друзей валиден");
        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(otherUser.getFriends());
        log.trace("Список общих друзей сформирован");
        return new ArrayList<>(intersection.stream().map((id1) -> userStorage
                .findById(id1).orElseThrow(() -> new NotFoundException("Такого id нет"))).toList());
    }


}
