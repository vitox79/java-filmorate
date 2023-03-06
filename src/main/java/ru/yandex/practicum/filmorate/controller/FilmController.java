package ru.yandex.practicum.filmorate.controller;

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
    FilmRepository filmRepository = new FilmRepository();

    final ValidateService validateService;
    public FilmController(ValidateService validateService){
        this.validateService = validateService;
    }
    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film, BindingResult bindingResult)throws ValidationException{
        if (bindingResult.hasErrors()) {
            log.error("Invalid film data");
            throw new ValidationException("Invalid film data");
        }
        validateService.validateFilm(film);
        filmRepository.save(film);
        return film;
    }
    @PutMapping(value = "/films")
    public Film updateFilm(@Valid  @RequestBody Film film, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("Invalid film data");
            throw new ValidationException("Invalid film data");
        }
        Film filmIn = filmRepository.getByID(film.getId());
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
        return filmRepository.getAll();
    }

}
