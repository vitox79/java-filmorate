package ru.yandex.practicum.filmorate.storage;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreData;
import ru.yandex.practicum.filmorate.model.RatingData;
import ru.yandex.practicum.filmorate.service.DataNotFoundException;
import ru.yandex.practicum.filmorate.service.UserNotFoundException;

import java.util.*;


@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("GenreDbStorage")
    private GenreStorage genres;

    @Autowired
    @Qualifier("MpaDbStorage")
    private MpaStorage mpa;

    private int count = 0;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genres, MpaStorage mpa) {

        this.mpa = mpa;
        this.genres = genres;
        this.jdbcTemplate = jdbcTemplate;
    }

    public String checkRating(Film film) {
        String query = "SELECT name FROM mpa WHERE id = ?";
        String rating = jdbcTemplate.queryForObject(query, String.class, film.getMpa().getId());
        if (rating == null) {
            throw new DataNotFoundException("Wrong rating data.");
        }
        return rating;
    }

    public void updateLikes(Film film) {

        for (int userId : film.getLikes()) {
            int count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM likes WHERE film_id = ? AND user_id = ?",
                    Integer.class,
                    film.getId(),
                    userId
            );
            if (count == 0) {
                jdbcTemplate.update(
                        "INSERT INTO likes (film_id, user_id) VALUES (?, ?)",
                        film.getId(),
                        userId
                );
            }
        }
    }

    public void getLikes(Film film) {

        List<Map<String, Object>> likesRows = jdbcTemplate.queryForList(
                "SELECT * FROM likes WHERE film_id = ?",
                film.getId()
        );
        Set<Integer> likes = new HashSet<>();
        for (
                Map<String, Object> likesRow : likesRows) {
            likes.add((int) likesRow.get("user_id"));
        }
        film.setLikes(likes);
    }

    @Override
    public void update(Film film) {

        String rating;
        if (film.getMpa() != null) {
            rating = checkRating(film);
            film.getMpa().setName(rating);

            jdbcTemplate.update(
                    "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ? WHERE id = ?",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getId()
            );
        }
        // update likes
        System.out.println(film);
        if (film.getLikes() != null) {
            updateLikes(film);
        }
        // update genres
        if (film.getGenres() != null) {
            genres.updateGenre(film);
        }
    }

    @Override
    public void save(Film film) {

        count++;
        film.setId(count);
        // insert new film into films table
        if (film.getMpa() != null) {
            String rating = checkRating(film);
            film.getMpa().setName(rating);
            jdbcTemplate.update(
                    "INSERT INTO films (name, description, release_date, duration,mpa_id , id) VALUES (?, ?, ?, ?, ?,?)",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );
        } else {
            jdbcTemplate.update(
                    "INSERT INTO films (name, description, release_date, duration , id) VALUES (?, ?, ?, ?,?)",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getId()
            );

        }
        genres.updateGenre(film);
    }

    @Override
    public Film getByID(int id) {

        try {
            Map<String, Object> filmsRow = jdbcTemplate.queryForMap(
                    "SELECT * FROM films WHERE id = ?",
                    id
            );
            Film film = Film.builder()
                    .id((int) filmsRow.get("id"))
                    .name((String) filmsRow.get("name"))
                    .description((String) filmsRow.get("description"))
                    .duration((long) filmsRow.get("duration"))
                    .releaseDate(((java.sql.Date) filmsRow.get("release_date")).toLocalDate())
                    .build();

            getLikes(film);
            genres.getGenre(film);
            mpa.getRating(film);
            return film;
        } catch (EmptyResultDataAccessException e) {
            String message = "Film id does not exist";
            throw new UserNotFoundException(message);
        }
    }

    @Override
    public List<Film> getAll() {

        List<Film> films = new ArrayList<>();
        String query = "SELECT f.*, " +
                "GROUP_CONCAT(DISTINCT g.id) AS genre_ids, " +
                "GROUP_CONCAT(DISTINCT g.name) AS genre_names, " +
                "GROUP_CONCAT(DISTINCT likes.user_id) AS likes, " +
                "m.id AS mpa_id, " +
                "m.name AS mpa_name " +
                "FROM films f " +
                "LEFT JOIN film_genre fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.id = fg.genre_id " +
                "LEFT JOIN MPA m ON m.id = f.mpa_id " +
                "LEFT JOIN LIKES likes ON likes.film_id = f.id " +
                "GROUP BY f.id, m.id";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        for (Map<String, Object> row : rows) {
            Film film = Film.builder()
                    .id((int) row.get("id"))
                    .name((String) row.get("name"))
                    .description((String) row.get("description"))
                    .releaseDate(((java.sql.Date) row.get("release_date")).toLocalDate())
                    .duration((long) row.get("duration"))
                    .build();
            if ((String) row.get("mpa_name") != null) {
                film.setMpa(new RatingData((int) row.get("mpa_id"),(String) row.get("mpa_name")));
            }

            Set<Integer> likes = new HashSet<>();
            String likesStr = (String) row.get("likes");
            if (likesStr != null) {
                String[] likeIds = likesStr.split(",");
                for (String likeId : likeIds) {
                    likes.add(Integer.parseInt(likeId));
                }
            }
            film.setLikes(likes);

            List<GenreData> genres = new ArrayList<>();
            String genreIdsStr = (String) row.get("genre_ids");
            String genreNamesStr = (String) row.get("genre_names");
            if (genreIdsStr != null && genreNamesStr != null) {
                String[] genreIds = genreIdsStr.split(",");
                String[] genreNames = genreNamesStr.split(",");
                for (int i = 0; i < genreIds.length; i++) {
                    int genreId = Integer.parseInt(genreIds[i]);
                    String genreName = genreNames[i];
                    genres.add(new GenreData(genreId, genreName));
                }
            }
            film.setGenres(genres);
            films.add(film);
        }
        return films;
    }
}

