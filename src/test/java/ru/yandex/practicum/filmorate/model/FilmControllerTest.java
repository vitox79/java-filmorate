package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.strorage.FilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {
    @Autowired
    @Qualifier("FilmDbStorage")
    FilmStorage repository;

    @Test
    void addFilm() {

        Film film = Film.builder()
                .name("name")
                .description("d")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(200)
                .build();
        repository.save(film);
        assertEquals(repository.getByID(film.getId()), film);
    }

    @Test
    void getFilms() {

        Film film = Film.builder()
                .name("name")
                .description("d")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(200)
                .build();
        repository.save(film);
        Film film2 = Film.builder()
                .name("name2")
                .description("d")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(200)
                .build();
        repository.save(film2);
        assertEquals(repository.getAll().size(), 2);
        assertEquals(repository.getByID(film2.getId()), film2);
    }
}