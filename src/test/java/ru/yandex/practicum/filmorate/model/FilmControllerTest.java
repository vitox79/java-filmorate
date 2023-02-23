package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.service.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private UserController userController;
    @Test
    void invalidDuration(){
        ValidateService validateService = new ValidateService();
        Film film = new Film();
        film.setDuration(-20);
        film.setDescription("d");
        film.setReleaseDate(LocalDate.of(2000,12,10));
        film.setName("name");
        assertThrows(ValidationException.class,()->validateService.validateFilm(film));
    }
    @Test
    void invalidDescription(){
        ValidateService validateService = new ValidateService();
        Film film = new Film();
        film.setDuration(200);
        StringBuilder builder = new StringBuilder();
        builder.setLength(300);
        film.setDescription(builder.toString());
        film.setReleaseDate(LocalDate.of(2000,12,10));
        film.setName("name");
        assertThrows(ValidationException.class,()->validateService.validateFilm(film));
    }



}