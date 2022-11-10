package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.practicum.filmorate.storage.SqlRequests.*;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getFilm(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_FILM_BY_ID, this::mapRowToFilm, id);
    }

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update(SQL_INSERT_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getLikes(), film.getGenre(), film.getRate().rating);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(SQL_UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getLikes(), film.getGenre(), film.getRate().rating, film.getId());
        return film;
    }

    @Override
    public List<Film> allFilms() {
        List<Film> films = jdbcTemplate.query(SQL_GET_ALL_FILMS, this::mapRowToFilm);

        for (Film film : films) {
            List<Long> likes = jdbcTemplate.queryForList(SQL_GET_LIKES_OF_FILM_BY_ID, Long.class, film.getId());
            film.setLikes(likes);
        }
        return films;
    }

    @Override
    public void deleteAllFilms() {
        jdbcTemplate.update(SQL_DELETE_ALL_FILMS);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
//                .likes(resultSet.getArray("likes")) TODO
                .genre(Genre.valueOfName(resultSet.getInt("genre")))
                .rate(Rating.valueOfName(resultSet.getInt("rating")))
                .build();
    }
}
