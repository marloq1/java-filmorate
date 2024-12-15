package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("UserDb")
    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User postUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.postUser(user);
    }

    public User putUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (userStorage.findById(user.getId()).isEmpty()) {
            throw new NotFoundException("Такого id нет");
        }
        return userStorage.putUser(user);
    }

    public User addFriend(Long id, Long friendId) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
        User friendUser = userStorage.findById(friendId).orElseThrow(() -> new NotFoundException("Такого id нет"));
        log.trace("id при добавлении валидны");
        userStorage.addFriend(id, friendId, friendUser.getFriends().contains(id));
        log.trace("Друг успешно добавлен");
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
    }

    public User deleteFriend(Long id, Long friendId) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
        User friendUser = userStorage.findById(friendId).orElseThrow(() -> new NotFoundException("Такого id нет"));
        log.trace("id при удалении валидны");
        userStorage.deleteFriend(id, friendId);
        //user.getFriends().remove(friendId);
        //friendUser.getFriends().remove(id);
        log.trace("Друг успешно удален");
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
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
