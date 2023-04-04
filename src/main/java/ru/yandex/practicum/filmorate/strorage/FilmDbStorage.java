package ru.yandex.practicum.filmorate.strorage;

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
    private int count = 0;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }


    public boolean filmExists(int id) {

        int num = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM films WHERE id = ?",
                Integer.class,
                id);
        return num > 0;
    }

    public void updateGenre(Film film) {

        // удаляем все записи о жанрах для данного фильма из таблицы film_genre
        String deleteGenresQuery = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(deleteGenresQuery, film.getId());
        String insertGenresQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        List<GenreData> genres = film.getGenres();
        if (genres != null) {

            for (GenreData genre : genres) {
                // проверяем, существует ли жанр в таблице genres
                String query = "SELECT EXISTS(SELECT 1 FROM genres WHERE id = ?)";
                boolean exists = jdbcTemplate.queryForObject(query, Boolean.class, genre.getId());
                query = "SELECT EXISTS(SELECT 1 FROM film_genre WHERE film_id = ? and genre_id =?)";
                exists = exists && !(jdbcTemplate.queryForObject(query, Boolean.class, film.getId(), genre.getId()));
                if (exists) {
                    System.out.println(genre.getId());
                    String name = jdbcTemplate.queryForObject(
                            "SELECT name FROM genres WHERE id = ?",
                            String.class,
                            genre.getId()
                    );

                    genre.setName(name);
                    // добавляем запись о связи между фильмом и жанром в таблицу film_genre
                    jdbcTemplate.update(insertGenresQuery, film.getId(), genre.getId());
                }
            }

        }
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

    public void getRating(Film film) {

        List<Map<String, Object>> ratingRows = jdbcTemplate.queryForList(
                "SELECT r.Id, r.Name FROM MPA r " +
                        "JOIN films f ON r.Id = f.mpa_id " +
                        "WHERE f.Id = ?",
                film.getId()
        );

        RatingData rating = null;
        if (!ratingRows.isEmpty()) {
            int ratingId = (int) ratingRows.get(0).get("Id");
            String ratingName = (String) ratingRows.get(0).get("Name");
            rating = new RatingData(ratingId, ratingName);
        }

        film.setMpa(rating);
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

    public void getGenre(Film film) {

        List<Map<String, Object>> genreRows = jdbcTemplate.queryForList(
                "SELECT g.Id, g.Name FROM Genres g " +
                        "JOIN film_genre fg ON g.Id = fg.genre_id " +
                        "WHERE fg.film_id = ?",
                film.getId()
        );

        List<GenreData> genres = new ArrayList<>();
        for (Map<String, Object> genreRow : genreRows) {
            int genreId = (int) genreRow.get("Id");
            String genreName = (String) genreRow.get("Name");
            genres.add(new GenreData(genreId, genreName));
        }
        film.setGenres(genres);
    }

    @Override
    public void save(Film film) {

        if (filmExists(film.getId())) {
            String rating = checkRating(film);
            System.out.println("RATING  " + rating);

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
            // update likes
            if (film.getLikes() != null) {
                updateLikes(film);
            }
            // update genres
            if (film.getGenres() != null) {
                updateGenre(film);
            }


        } else {
            // insert new film into films table
            String rating = checkRating(film);
            count++;
            film.setId(count);
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
            updateGenre(film);

        }
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
            getGenre(film);
            getRating(film);
            return film;
        } catch (EmptyResultDataAccessException e) {
            String message = "Film id does not exist";
            throw new UserNotFoundException(message);
        }
    }

    @Override
    public List<Film> getAll() {

        List<Film> films = new ArrayList<>();
        List<Map<String, Object>> filmsRows = jdbcTemplate.queryForList(
                "SELECT * FROM films"
        );
        for (Map<String, Object> filmsRow : filmsRows) {
            int id = (int) filmsRow.get("id");
            Film film = getByID(id);
            films.add(film);
        }
        return films;
    }

    @Override
    public GenreData getGenre(int id) {

        List<Map<String, Object>> genreRows = jdbcTemplate.queryForList(
                "SELECT * FROM Genres WHERE Id = ?", id
        );
        if (genreRows.isEmpty()) {
            throw new DataNotFoundException("Data not found");
        }
        Map<String, Object> genreRow = genreRows.get(0);
        int genreId = (int) genreRow.get("id");
        String genreName = (String) genreRow.get("name");
        return new GenreData(genreId, genreName);
    }

    @Override
    public RatingData getRating(int id) {

        List<Map<String, Object>> mpaRows = jdbcTemplate.queryForList(
                "SELECT * FROM MPA WHERE Id = ?", id
        );
        if (mpaRows.isEmpty()) {
            throw new DataNotFoundException("Rating id does not exist");
        }
        Map<String, Object> genreRow = mpaRows.get(0);
        id = (int) genreRow.get("id");
        String name = (String) genreRow.get("name");
        return new RatingData(id, name);
    }

    @Override
    public List<RatingData> getRatingAll() {

        List<Map<String, Object>> mpaRows = jdbcTemplate.queryForList(
                "SELECT * FROM MPA"
        );
        if (mpaRows.isEmpty()) {
            throw new DataNotFoundException("Data not found");
        }
        List<RatingData> ratingDataList = new ArrayList<>();
        for (Map<String, Object> genreRow : mpaRows) {
            int id = (int) genreRow.get("id");
            String name = (String) genreRow.get("name");
            System.out.println("   " + name);
            ratingDataList.add(new RatingData(id, name));
        }
        return ratingDataList;
    }

    @Override
    public List<GenreData> getGenreAll() {

        List<Map<String, Object>> genreRows = jdbcTemplate.queryForList(
                "SELECT * FROM Genres"
        );
        if (genreRows.isEmpty()) {
            throw new DataNotFoundException("Data not found");
        }
        List<GenreData> genreDataList = new ArrayList<>();
        for (Map<String, Object> genreRow : genreRows) {
            int id = (int) genreRow.get("id");
            String name = (String) genreRow.get("name");
            genreDataList.add(new GenreData(id, name));
        }
        return genreDataList;
    }
}