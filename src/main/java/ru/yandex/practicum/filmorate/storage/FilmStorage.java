package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreData;
import ru.yandex.practicum.filmorate.model.RatingData;

import java.util.List;

public interface FilmStorage {
    void save(Film film);

    List<Film> getAll();

    GenreData getGenre(int id);

    RatingData getRating(int id);

    List<GenreData> getGenreAll();

    List<RatingData> getRatingAll();

    Film getByID(int id);

}