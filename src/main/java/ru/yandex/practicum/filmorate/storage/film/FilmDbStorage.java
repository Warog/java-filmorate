package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.SqlRequests.*;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Film getFilm(int id) {
        Film film = jdbcTemplate.queryForObject(SQL_GET_FILM_BY_ID, this::mapRowToFilm, id);
        List<Genre> genres = jdbcTemplate.query(SQL_GET_FILM_GENRES_BY_ID, this::mapRowToGenre, id);

        List<Long> likes = jdbcTemplate.queryForList(SQL_GET_LIKES_OF_FILM_BY_ID, Long.class, film.getId());
        film.setLikes(likes);

        film.setGenres(genres);

        return film;
    }

    @Override
    @Transactional
    public Film addFilm(Film film) {
        Film validatedFilm = FilmValidator.validateFilmInfo(film);
        jdbcTemplate.update(SQL_INSERT_FILM, validatedFilm.getName(), validatedFilm.getDescription(), validatedFilm.getReleaseDate(), validatedFilm.getDuration(), validatedFilm.getLikes(), validatedFilm.getMpa().getId(), validatedFilm.getRate());

        Integer maxId = jdbcTemplate.queryForObject(SQL_GET_MAX_FILM_ID, Integer.class);
        validatedFilm.setId(maxId);

        validatedFilm.getGenres()
                .forEach(
                        genre -> jdbcTemplate.update(SQL_SET_GENRE_TO_FILM, validatedFilm.getId(), genre.getId())
                );


        log.info("Фильм добавлен: {}", validatedFilm);
        return validatedFilm;
    }

    @Override
    @Transactional
    public Film updateFilm(Film film) {
        Film validatedFilm = FilmValidator.validateFilmInfo(film);
        if (FilmValidator.validateFilmExistsInDB(jdbcTemplate, validatedFilm)) {
            jdbcTemplate.update(SQL_UPDATE_FILM, validatedFilm.getName(), validatedFilm.getDescription(), validatedFilm.getReleaseDate(), validatedFilm.getDuration(), validatedFilm.getLikes(), validatedFilm.getMpa().getId(), validatedFilm.getRate(), validatedFilm.getId());
        } else {
            throw new UserNotFoundException("Невозможно обновить данные фильма!");
        }

        return validatedFilm;
    }

    @Override
    @Transactional
    public List<Film> allFilms() {
        log.info("Вывод всех фильмов");

        List<Film> films = jdbcTemplate.query(SQL_GET_ALL_FILMS, this::mapRowToFilm);

        for (Film film : films) {
            List<Long> likes = jdbcTemplate.queryForList(SQL_GET_LIKES_OF_FILM_BY_ID, Long.class, film.getId());
            film.setLikes(likes);
        }
        return films;
    }

    @Override
    public void setLike(int filmId, long userId) {
        jdbcTemplate.update(SQL_SET_USER_LIKE_TO_FILM, filmId, userId);
        log.info("Лайк! UserID = {} лайкнул FilmID = {}", userId, filmId);
    }

    @Override
    public void unsetLike(int filmId, long userId) {
        jdbcTemplate.update(SQL_DELETE_USER_LIKE_OF_FILM, filmId, userId);
        log.info("Unlike! UserID = {} отобрал лайк у  FilmID = {}", userId, filmId);
    }

    @Override
    public void deleteAllFilms() {
        jdbcTemplate.update(SQL_DELETE_ALL_FILMS);

        log.info("Все фильмы удалены");
    }

    @Override
    @Transactional
    public List<Film> getMostPopularFilms(int limit) {
        List<Integer> integers = jdbcTemplate.query(SQL_GET_POPULAR_FILMS_WITH_LIMIT, (rs, rowNum) -> rs.getInt("FILM_ID"), limit);

        List<Film> films = integers.stream().map(filmId -> jdbcTemplate.queryForObject(SQL_GET_FILM_BY_ID, this::mapRowToFilm, filmId)).collect(Collectors.toList());

        for (Film film : films) {
            List<Long> likes = jdbcTemplate.queryForList(SQL_GET_LIKES_OF_FILM_BY_ID, Long.class, film.getId());
            film.setLikes(likes);
        }

        return films;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("genre"));

        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
//                .likes(resultSet.getArray("likes")) TODO
//                .genres(resultSet.getInt("genre"))
                .mpa(mpa)
                .rate(resultSet.getInt("rating"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .build();
    }
}
