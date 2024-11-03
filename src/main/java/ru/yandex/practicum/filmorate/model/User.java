package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of ={"email"})
@NoArgsConstructor
public class User {


  private Long id;
  @NonNull
  @NotBlank
  @Email
  private String email;
  @NonNull
  @NotBlank
  @Pattern(regexp = "^\\S*$")
  private String login;
  private String name;
  @NonNull
  @Past
  private LocalDate birthday;


}
