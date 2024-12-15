package ru.yandex.practicum.filmorate.model.user;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"email"})
@NoArgsConstructor
public class User {

  private Long id;
  private String email;
  private String login;
  private String name;
  private LocalDate birthday;
  private Set<Long> friends = new HashSet<>();


}
