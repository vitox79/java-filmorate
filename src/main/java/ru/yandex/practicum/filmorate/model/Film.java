package ru.yandex.practicum.filmorate.model;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
@Data
@Builder
public class Film {
    private  int id;

    @NotBlank
    @NonNull
    private String name;

    @NotBlank
    @NonNull
    private String description;
    @NonNull
    @PastOrPresent(message = "Дата релиза не может быть в будущем. ")
    private LocalDate releaseDate;
    @Length(min = 1, max = 200, message = "Описание фильма не должно превышать 200 символов.")
    @Min(value = 0, message = "Продолжительность фильма не может быть отрицательной.")
    private long duration;
}
