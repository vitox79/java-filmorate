package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {

    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void validDescription() {
        Film film = Film.builder()
                .name("name")
                .description("")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(200)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        System.out.println(violations);
        assertEquals(1, violations.size(), "Invalid description");
    }

    @Test
    void validName() {
        Film film = Film.builder()
                .name("")
                .description("description")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(200)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size(), "Invalid name");
    }

    @Test
    void validDescriptionLength() {
        StringBuilder builder = new StringBuilder();
        builder.setLength(300);
        String description = builder.toString();
        Film film = Film.builder()
                .name("name")
                .description(description)
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(200)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        System.out.println(violations);
        assertEquals(1, violations.size(), "Invalid description");
    }
}