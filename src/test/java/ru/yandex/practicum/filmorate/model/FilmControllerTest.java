package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.strorage.InMemoryFilmStorage;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void addFilm(){
        InMemoryFilmStorage repository = new InMemoryFilmStorage();
        Film film = Film.builder()
                .name("name")
                .description("d")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(200)
                .build();
        repository.save(film);
        assertEquals(repository.getByID(film.getId()),film);
    }
    @Test
    void getFilms(){
        InMemoryFilmStorage repository = new InMemoryFilmStorage();
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
        assertEquals(repository.getAll().size(),2);
        assertEquals(repository.getByID(film2.getId()),film2);
    }
}