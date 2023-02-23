package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.NonNull;

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
