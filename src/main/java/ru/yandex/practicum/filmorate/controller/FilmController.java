package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.ValidationException;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;

@RestController
@Slf4j
public class FilmController {
    FilmRepository films = new FilmRepository();
    private int count = 0;
    final ValidateService validateService;
    public FilmController(ValidateService validateService){
        this.validateService = validateService;
    }
    private void checkFilm(Film film)throws ValidationException {

        if (film.getDescription().length() > 200) {
            String message = "Film description must be less than or equal to 200 characters";
            log.error(message);
            throw new ValidationException(message);
        }

        Date oldestReleaseDate = new Date(-2208992400000L); // December 28, 1895
        if ( film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String message = "Film release date cannot be before December 28, 1895";
            log.error(message);
            throw new ValidationException(message);
        }
        if (film.getDuration() <= 0) {
            String message = "Film duration must be positive";
            log.error(message);
            throw new ValidationException(message);
        }
    }
    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film, BindingResult bindingResult)throws ValidationException{
        if (bindingResult.hasErrors()) {
            log.error("Invalid film data");
            throw new ValidationException("Invalid film data");
        }
        validateService.validateFilm(film);
        count++;
        film.setId(count);
        films.save(film);
        return film;
    }
    @PutMapping(value = "/films")
    public Film updateFilm(@Valid  @RequestBody Film film, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("Invalid film data");
            throw new ValidationException("Invalid film data");
        }
        Film filmIn = films.getByID(film.getId());
        if (filmIn == null) {
            String message  = "Film id does not exist";
            log.error(message);
            throw new ValidationException(message);
        }
        validateService.validateFilm(film);
        filmIn.setDescription(film.getDescription());
        filmIn.setName(film.getName());
        filmIn.setDuration(film.getDuration());
        filmIn.setReleaseDate(film.getReleaseDate());
        return filmIn;
    }
    @GetMapping("/films")
    public List<Film> allFilms(){
        return films.getAll();
    }

}
