package ru.yandex.practicum.filmorate.model;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Data
@Builder
public class User {
    private int id;
    @Email(message = "Email should be valid")
    private String email;
    @NotEmpty(message = "Invalid login")
    @Pattern(regexp = "\\S*", message = "Логин не может содержать пробелы.")
    private String login;
    private  String name;
    @Past(message = "День рождения должно быть в прошлом.")
    private LocalDate birthday;
    private Set<Integer> friends;
    private Map<Integer, FriendshipStatus> friendshipStatuses;
}
