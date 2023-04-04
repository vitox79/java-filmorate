package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreData;
import ru.yandex.practicum.filmorate.model.RatingData;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final ValidateService validateService;
    private final FilmService filmService;

    private final UserService users;

    @Autowired
    public FilmController(ValidateService validateService, FilmService filmService, UserService users) {
        this.users = users;
        this.filmService = filmService;
        this.validateService = validateService;
    }

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            log.error("Invalid film data");
            throw new ValidationException("Invalid film data");
        }
        System.out.println(film);
        validateService.validateFilm(film);
        filmService.save(film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("Invalid film data");
            throw new ValidationException("Invalid film data");
        }
        validateService.validateFilm(film);
        Film filmIn = filmService.getByID(film.getId());
        if (filmIn == null) {
            String message = "Film id does not exist";
            log.error(message);
            throw new FilmNotFoundException(message);
        }
        filmIn.setDescription(film.getDescription());
        filmIn.setName(film.getName());
        filmIn.setDuration(film.getDuration());
        filmIn.setReleaseDate(film.getReleaseDate());
        filmIn.setMpa(film.getMpa());
        filmIn.setGenres(film.getGenres());
        filmService.updateFilm(filmIn);
        filmIn = getFilm(filmIn.getId());
        return filmIn;
    }

    @GetMapping("/films")
    public List<Film> allFilms() {
        return filmService.getAll();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        Film film = filmService.getByID(id);
        if (film == null) {
            String message = "Film id does not exist";
            log.error(message);
            throw new FilmNotFoundException(message);
        }
        return filmService.getByID(id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void unlikeFilm(@PathVariable int id, @PathVariable int userId) {
        Film film = filmService.getByID(id);
        if (film == null) {
            String message = "Film id does not exist";
            log.error(message);
            throw new FilmNotFoundException(message);
        }
        User user = users.getByID(userId);
        if (user == null) {
            String message = "User id does not exist";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        filmService.removeLike(id, userId);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        Film film = filmService.getByID(id);
        if (film == null) {
            String message = "Film id does not exist";
            log.error(message);
            throw new FilmNotFoundException(message);
        }
        User user = users.getByID(userId);
        if (user == null) {
            String message = "User id does not exist";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        filmService.addLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("/genres/{id}")
    public GenreData getGenre(@PathVariable int id) {

        return filmService.getGenre(id);
    }

    @GetMapping("/mpa/{id}")
    public RatingData getRating(@PathVariable int id) {

        return filmService.getRating(id);
    }

    @GetMapping("/genres")
    public List<GenreData> getGenreAll() {

        return filmService.getGenreAll();
    }

    @GetMapping("/mpa")
    public List<RatingData> getRatingAll() {

        return filmService.getRatingAll();
    }


}
