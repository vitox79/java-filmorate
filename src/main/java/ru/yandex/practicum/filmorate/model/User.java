package ru.yandex.practicum.filmorate.model;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;


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
    @Past
    private LocalDate birthday;
}
