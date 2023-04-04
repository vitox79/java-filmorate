package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.strorage.FilmStorage;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FilmService {
    @Autowired
    @Qualifier("FilmDbStorage")
    private FilmStorage films;

    public void updateFilm(Film film) {

        films.save(film);
    }
    public void addLike(int filmId, int userId) {
        Film film = films.getByID(filmId);
        if (film == null) {
            throw new FilmNotFoundException("Film id does not exit");
        }
        Set<Integer> likes = film.getLikes();
        if (likes == null) {
            likes = new HashSet<>();
            film.setLikes(likes);
        }
        film.getLikes().add(userId);
        films.save(film);
    }

    public void removeLike(int filmId, int userId) {
        Film film = films.getByID(filmId);

        if (film != null) {
            if (film.getLikes()==null) {
                return;
            }
            film.getLikes().remove(userId);
            films.save(film);
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
    public GenreData getGenre(int id){

        return films.getGenre(id);
    }
    public List<GenreData> getGenreAll(){

        return films.getGenreAll();
    }

    public RatingData getRating(int id){

        return films.getRating(id);
    }
    public List<RatingData> getRatingAll(){
        return films.getRatingAll();}
    public Film getByID(int id) {

        return films.getByID(id);
    }

    public List<Film> getAll() {

        return films.getAll();
    }

}
