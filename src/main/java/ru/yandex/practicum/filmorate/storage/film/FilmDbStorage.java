package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.SqlRequests.*;

@Slf4j
@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Film getFilm(int id) {
        try {
            Optional<Film> film = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_FILM_BY_ID, this::mapRowToFilm, id));

            if (film.isEmpty()) {
                throw new FilmNotFoundException("Фильм с ID = " + id + " не найден в БД");
            } else {
                List<Long> likes = jdbcTemplate.queryForList(SQL_GET_LIKES_OF_FILM_BY_ID, Long.class, film.get().getId());
                film.get().setLikes(likes);
                film.get().setGenres(jdbcTemplate.query(SQL_GET_FILM_GENRES_BY_ID, this::mapRowToGenre, id));
                film.get().setMpa(getMpaById(film.get().getMpa().getId()));

                return film.get();
            }

        } catch (
                EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("Фильм с ID = " + id + " не найден в БД");
        }
    }

    @Override
    @Transactional
    public Film addFilm(Film film) {
        Film validatedFilm = FilmValidator.validateFilmInfo(film);

        jdbcTemplate.update(SQL_INSERT_FILM, validatedFilm.getName(), validatedFilm.getDescription(), validatedFilm.getReleaseDate(), validatedFilm.getDuration(), validatedFilm.getRate(), validatedFilm.getMpa().getId());

        Integer maxId = jdbcTemplate.queryForObject(SQL_GET_MAX_FILM_ID, Integer.class);
        if (maxId == null) {
            throw new NullPointerException("Невезможно считать максиальное значение поля ID");
        }
        validatedFilm.setId(maxId);

        if (validatedFilm.getGenres() != null) {
            validatedFilm.getGenres()
                    .forEach(
                            genre -> jdbcTemplate.update(SQL_SET_GENRE_TO_FILM, validatedFilm.getId(), genre.getId())
                    );
        }

        log.info("Фильм добавлен: {}", validatedFilm);
        return validatedFilm;
    }

    @Override
    @Transactional
    public Film updateFilm(Film film) {
        Film validatedFilm = FilmValidator.validateFilmInfo(film);
        if (FilmValidator.validateFilmExistsInDB(jdbcTemplate, validatedFilm)) {

            validatedFilm.getMpa().setName(getMpaById(validatedFilm.getMpa().getId()).getName());
            jdbcTemplate.update(SQL_UPDATE_FILM, validatedFilm.getName(), validatedFilm.getDescription(), validatedFilm.getReleaseDate(), validatedFilm.getDuration(), validatedFilm.getRate(), validatedFilm.getMpa().getId(), validatedFilm.getId());

            if (validatedFilm.getGenres() != null && validatedFilm.getGenres().size() > 0) {
                // ИЗ БД
                List<Integer> genresOfDb = jdbcTemplate.query(SQL_GET_FILM_GENRES_BY_ID, this::mapRowToGenre, validatedFilm.getId()).stream()
                        .map(Genre::getId).collect(Collectors.toList());
                // ИЗ ЗАПРОСА
                List<Integer> genres = validatedFilm.getGenres().stream()
                        .map(Genre::getId).distinct().sorted().collect(Collectors.toList());

                genres.stream()
                        .filter(integer -> !genresOfDb.contains(integer))
                        .collect(Collectors.toList())
                        .forEach(id -> jdbcTemplate.update(SQL_SET_GENRE_TO_FILM, validatedFilm.getId(), id));

                genresOfDb.stream()
                        .filter(integer -> !genres.contains(integer))
                        .collect(Collectors.toList())
                        .forEach(id -> jdbcTemplate.update(SQL_DELETE_GENRE_OF_FILM_WITH_ID, validatedFilm.getId(), id));

                List<Genre> finalGenres = validatedFilm.getGenres().stream().distinct().sorted().collect(Collectors.toList());
                finalGenres.forEach(genre -> genre.setName(getGenreById(genre.getId()).getName()));
                validatedFilm.setGenres(finalGenres);

            } else {
                jdbcTemplate.update(SQL_DELETE_ALL_GENRE_OF_FILM_WITH_ID, film.getId());
            }
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
            film.setLikes(jdbcTemplate.queryForList(SQL_GET_LIKES_OF_FILM_BY_ID, Long.class, film.getId()));
            film.setMpa(getMpaById(film.getMpa().getId()));
            film.setGenres(jdbcTemplate.query(SQL_GET_FILM_GENRES_BY_ID, this::mapRowToGenre, film.getId()));
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

        List<Film> films = integers.stream()
                .map(filmId -> jdbcTemplate.queryForObject(SQL_GET_FILM_BY_ID, this::mapRowToFilm, filmId))
                .collect(Collectors.toList());

        if (films.size() == 0) {
            return allFilms();
        } else {
            for (Film film : films) {
                List<Long> likes = jdbcTemplate.queryForList(SQL_GET_LIKES_OF_FILM_BY_ID, Long.class, film.getId());
                film.setLikes(likes);
                film.setMpa(getMpaById(film.getMpa().getId()));
            }
        }

        return films;
    }

    @Override
    public Mpa getMpaById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_MPA, this::mapRowToMpa, id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(SQL_GET_ALL_MPA, this::mapRowToMpa);
    }

    @Override
    public Genre getGenreById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_GENRE, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query(SQL_GET_ALL_GENRES, this::mapRowToGenre);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {

        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("rating"), null))
                .rate(resultSet.getInt("rate"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
