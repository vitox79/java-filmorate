package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

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
