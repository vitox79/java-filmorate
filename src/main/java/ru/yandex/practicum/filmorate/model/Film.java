package ru.yandex.practicum.filmorate.model;
import lombok.NonNull;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@lombok.Getter
@lombok.Setter
@lombok.ToString
@lombok.EqualsAndHashCode

public class Film {
    private  int id;

    @NotBlank
    @NonNull
    private String name;

    @NotBlank
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    private long duration;
}
