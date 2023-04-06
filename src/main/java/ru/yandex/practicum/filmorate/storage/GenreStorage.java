package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreData;

import java.util.List;

public interface GenreStorage {

    GenreData getGenreByID(int id);

    List<GenreData> getGenreAll();

     void getGenre(Film film);

    void updateGenre(Film film);
}
