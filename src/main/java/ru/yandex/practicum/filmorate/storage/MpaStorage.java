package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingData;

import java.util.List;

public interface MpaStorage {

    void getRating(Film film);

    RatingData getRating(int id);

    List<RatingData> getRatingAll();
}
