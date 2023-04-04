package ru.yandex.practicum.filmorate.model;
import lombok.Data;

@Data
public class RatingData {
    int id;
    String name;

    public RatingData(int ratingId, String valueOf) {
        id = ratingId;
        name = valueOf;
    }
}
