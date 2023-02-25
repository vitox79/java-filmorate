package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.service.ValidationException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void invalidDuration(){
        ValidateService validateService = new ValidateService();
       Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(-20)
                .build();
        assertThrows(ValidationException.class,()->validateService.validateFilm(film));
    }
    @Test
    void invalidDescription(){
        ValidateService validateService = new ValidateService();
        StringBuilder description = new StringBuilder();
        description.setLength(300);
        Film film  = Film.builder()
                .name("name")
                .description(description.toString())
                .releaseDate(LocalDate.of(2002, 12, 12))
                .duration(-20)
                .build();
        film.setDuration(200);
        film.setReleaseDate(LocalDate.of(2000,12,10));
        film.setName("name");
        assertThrows(ValidationException.class,()->validateService.validateFilm(film));
    }



}