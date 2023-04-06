package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreData;
import ru.yandex.practicum.filmorate.model.RatingData;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmServiceTest {
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private FilmStorage filmStorage;

    private GenreStorage genres;
    private MpaStorage mpa;

    @BeforeEach
    void init() {

        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        genres = new GenreDbStorage(jdbcTemplate);
        mpa = new MpaDbStorage(jdbcTemplate);
        filmStorage = new FilmDbStorage(jdbcTemplate, genres,mpa);
    }

    List<Film> makeFilms() {

        List<Film> films = new ArrayList<>();
        Film film1 = Film.builder()
                .id(1)
                .name("name")
                .description("d")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(200)
                .build();

        Film film2 = Film.builder()
                .name("name2")
                .description("d")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(200)
                .build();
        film1.setGenres(new ArrayList<>());
        film2.setGenres(new ArrayList<>());
        films.add(film1);
        films.add(film2);
        return films;
    }

    @AfterEach
    void shutDown() {

        embeddedDatabase.shutdown();
    }

    @Test
    void updateFilm() {

        List<Film> films = new ArrayList<>();
        films = makeFilms();
        filmStorage.save(films.get(0));
        films.get(1).setId(1);
        filmStorage.update(films.get(1));
        Film film = filmStorage.getByID(1);
        assertEquals(film.getName(),films.get(1).getName());
    }

    @Test
    void save() {

        List<Film> films = makeFilms();
        filmStorage.save(films.get(0));
        filmStorage.save(films.get(1));
        Film film = filmStorage.getByID(2);
        assertTrue(film.getName().equals(films.get(1).getName()));
    }

    @Test
    void getGenre() {

        GenreData genre = genres.getGenreByID(1);
        assertTrue(genre.getName().equals("Комедия"));

    }

    @Test
    void getGenreAll() {

        List<GenreData> genreList = genres.getGenreAll();
        assertEquals(genreList.size(),6);
        assertTrue(genreList.get(0).getName().equals("Комедия"));
    }

    @Test
    void getRatingAll() {

        List<RatingData> ratingDataList = mpa.getRatingAll();
        assertEquals(ratingDataList.size(), 5);
    }

    @Test
    void getByID() {

        List<Film> films = makeFilms();
        filmStorage.save(films.get(0));
        filmStorage.save(films.get(1));
        Film film = filmStorage.getByID(films.get(0).getId());
        assertTrue(film.getName().equals(films.get(0).getName()));
        assertTrue(film.getDescription().equals(films.get(0).getDescription()));
    }

    @Test
    void getAll() {

        List<Film> films = makeFilms();
        filmStorage.save(films.get(0));
        filmStorage.save(films.get(1));
        List<Film> filmList = filmStorage.getAll();
        assertEquals(filmList.size(),2);
        assertTrue(filmList.get(0).getName().equals(films.get(0).getName()));
        assertTrue(filmList.get(0).getDescription().equals(films.get(0).getDescription()));

    }
}