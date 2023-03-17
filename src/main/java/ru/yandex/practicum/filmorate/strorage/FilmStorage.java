package ru.yandex.practicum.filmorate.strorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void save(Film film);

    List<Film> getAll();

    Film getByID(int id);

}
