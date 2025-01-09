package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("UserDb")
    private final UserStorage userStorage;

    public Collection<UserDto> getUsers() {

        return userStorage.getUsers().stream().map(UserMapper::mapToUserDto).toList();
    }

    public UserDto postUser(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            userDto.setName(userDto.getLogin());
        }
        User user = UserMapper.mapToUser(userDto);
        return UserMapper.mapToUserDto(userStorage.postUser(user));
    }

    public UserDto putUser(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank()) {
            userDto.setName(userDto.getLogin());
        }
        User user = UserMapper.mapToUser(userDto);
        user.setId(userDto.getId());
        if (userStorage.findById(user.getId()).isEmpty()) {
            throw new NotFoundException("Такого id нет");
        }

        return UserMapper.mapToUserDto(userStorage.putUser(user));
    }

    public UserDto getUser(long id) {
        return UserMapper.mapToUserDto(userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет")));
    }

    public UserDto addFriend(Long id, Long friendId) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
        User friendUser = userStorage.findById(friendId).orElseThrow(() -> new NotFoundException("Такого id нет"));
        log.trace("id при добавлении валидны");
        userStorage.addFriend(id, friendId, friendUser.getFriends().contains(id));
        log.trace("Друг успешно добавлен");
        return UserMapper.mapToUserDto(userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет")));
    }

    public UserDto deleteFriend(Long id, Long friendId) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
        User friendUser = userStorage.findById(friendId).orElseThrow(() -> new NotFoundException("Такого id нет"));
        log.trace("id при удалении валидны");
        userStorage.deleteFriend(id, friendId);
        log.trace("Друг успешно удален");
        return UserMapper.mapToUserDto(userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет")));
    }

    public List<UserDto> getUserFriends(Long id) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
        log.trace("id при получении списка друзей валиден");
        return new ArrayList<>(user.getFriends().stream()
                .map((id1) -> userStorage.findById(id1).orElseThrow(() -> new NotFoundException("Такого id нет")))
                .map(UserMapper::mapToUserDto)
                .toList());
    }

    public List<UserDto> getMutualFriendsList(Long id, Long otherId) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Такого id нет"));
        User otherUser = userStorage.findById(otherId).orElseThrow(() -> new NotFoundException("Такого id нет"));
        log.trace("id при получении общих друзей валиден");
        Set<Long> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(otherUser.getFriends());
        log.trace("Список общих друзей сформирован");
        return new ArrayList<>(intersection.stream().map((id1) -> userStorage
                        .findById(id1).orElseThrow(() -> new NotFoundException("Такого id нет")))
                .map(UserMapper::mapToUserDto).toList());
    }


}
