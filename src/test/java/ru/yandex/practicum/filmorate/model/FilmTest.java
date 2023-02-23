package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
    @Test
    void validDescription(){
        Film film = new Film();
        film.setDuration(200);
        film.setDescription("");
        film.setReleaseDate(LocalDate.of(2000,12,10));
        film.setName("name");;
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1,violations.size(),"Invalid login");
    }
    @Test
    void validName(){
        Film film = new Film();
        film.setDuration(200);
        film.setDescription("D");
        film.setReleaseDate(LocalDate.of(2000,12,10));
        film.setName("");;
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1,violations.size(),"Invalid login");
    }

}