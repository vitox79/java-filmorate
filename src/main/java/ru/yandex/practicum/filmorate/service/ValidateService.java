package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;

@Component
@Slf4j
public class ValidateService {

    public void validateUser(User user){
        if (user == null) {
            String message  = "User cannot be null";
            log.error(message);
            throw new ValidationException(message);
        }

        if (user.getName() == null || user.getName().isEmpty()||user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public void validateFilm(Film film){

            LocalDate oldestReleaseDate = LocalDate.of(1895,12,28);if ( film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String message = "Film release date cannot be before December 28, 1895";
            log.error(message);
            throw new ValidationException(message);
        }

    }

}
