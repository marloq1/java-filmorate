package ru.yandex.practicum.filmorate.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"email"})
@NoArgsConstructor
public class UserDao {

    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Long receiverId;
    private boolean isApproved;


}
