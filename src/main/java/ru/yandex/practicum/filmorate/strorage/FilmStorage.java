package ru.yandex.practicum.filmorate.strorage;
import ru.yandex.practicum.filmorate.model.*;

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
