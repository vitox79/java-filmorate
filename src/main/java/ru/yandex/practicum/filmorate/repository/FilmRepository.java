package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FilmRepository {
    HashMap<Integer, Film> films = new HashMap<>();
    public void save(Film film){
        films.put(film.getId(),film);
    }
    public Film getByID(int id){
        return films.get(id);
    }
    public int size(){
        return films.size();
    }
    public List<Film> getAll(){
        return films.values().stream().collect(Collectors.toList());
    }
}
