package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.GenreData;
import ru.yandex.practicum.filmorate.service.DataNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("GenreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;


    public GenreDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
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

    @Override
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
    public GenreData getGenreByID(int id) {

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
