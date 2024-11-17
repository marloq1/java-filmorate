package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.*;
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
  @NotNull
  @NotBlank
  @Email
  private String email;
  @NotNull
  @NotBlank
  @Pattern(regexp = "^\\S*$")
  private String login;
  private String name;
  @NotNull
  @Past
  private LocalDate birthday;
  private Set<Long> friends = new HashSet<>();


}
