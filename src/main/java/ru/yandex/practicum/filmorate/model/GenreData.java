package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class GenreData {

    String name;
    int id;

    public GenreData(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
