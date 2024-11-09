package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    static User user;
    static UserController userController = new UserController();

    @BeforeEach
    public void addValidFilmTest() {
        user = new User();
        user.setName("1");
        user.setEmail("1@mail.ru");
        user.setLogin("a".repeat(100));
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("Fedor");
        assertEquals(userController.postUser(user), user);
    }

    @Test
    public void replaceInvalidIdFilmTest() {
        user = new User();
        user.setName("1");
        user.setEmail("2@mail.ru");
        user.setLogin("a".repeat(100));
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("Basil");
        user.setId(3L);
        assertThrows(ValidationException.class, () -> userController.putUser(user));
    }

    @Test
    public void replaceNoIdFilmTest() {
        user = new User();
        user.setName("1");
        user.setEmail("2@mail.ru");
        user.setLogin("a".repeat(100));
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("Basil");
        assertThrows(ValidationException.class, () -> userController.putUser(user));
    }

    @Test
    public void replaceValidIdFilmTest() {
        user = new User();
        user.setName("1");
        user.setEmail("2@mail.ru");
        user.setLogin("a".repeat(100));
        user.setBirthday(LocalDate.of(2000, 1, 1));
        user.setName("Basil");
        user.setId(1L);
        assertEquals(userController.putUser(user), user);

    }

}