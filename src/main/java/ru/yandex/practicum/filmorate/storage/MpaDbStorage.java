package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingData;
import ru.yandex.practicum.filmorate.service.DataNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("MpaDbStorage")
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;


    public MpaDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
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
            ratingDataList.add(new RatingData(id, name));
        }
        return ratingDataList;
    }




}
