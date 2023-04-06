package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RatingData {
    int id;

    @NotBlank
    String name;

    public RatingData(int ratingId, String valueOf) {
        id = ratingId;
        name = valueOf;
    }
}
