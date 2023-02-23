package ru.yandex.practicum.filmorate.model;


import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;


@lombok.Getter
@lombok.Setter
@lombok.ToString
@lombok.EqualsAndHashCode

public class User {
    private int id;
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Invalid login")
    @NonNull
    private String login;
    private  String name;
    @Past
    private LocalDate birthday;
}
