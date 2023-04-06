package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GenreData {

    int id;

    @NotBlank
    String name;

    public GenreData(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
