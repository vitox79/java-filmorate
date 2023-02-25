package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FilmRepository {
    HashMap<Integer, Film> films = new HashMap<>();
    private int count = 0;
    public void save(Film film){
        count++;
        film.setId(count);
        films.put(count,film);
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
