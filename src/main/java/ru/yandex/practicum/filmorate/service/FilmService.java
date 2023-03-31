package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.strorage.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
public class FilmService {
    @Autowired
    private InMemoryFilmStorage films;


    public void addLike(int filmId, int userId) {
        Film film = films.getByID(filmId);
        if (film == null) {
            throw new FilmNotFoundException("Film id does not exit");
        }
        film.getLikes().add(userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = films.getByID(filmId);

        if (film != null) {
            film.getLikes().remove(userId);
        }
    }

    public List<Film> getTopFilms(int count) {
        List<Film> filmList = films.getAll();
        filmList.sort(Comparator.comparingInt(f -> -f.getLikes().size()));
        return filmList.subList(0, Math.min(count, filmList.size()));
    }

    public void save(Film film) {
        film.setLikes(new HashSet<>());
        films.save(film);
    }

    public Film getByID(int id) {
        return films.getByID(id);
    }

    public List<Film> getAll() {
        return films.getAll();
    }

}
