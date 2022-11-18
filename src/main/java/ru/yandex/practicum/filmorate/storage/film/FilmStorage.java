package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {
    Film getFilm(int id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> allFilms();

    void setLike(int filmId, long userId);

    void unsetLike(int filmId, long userId);

    void deleteAllFilms();

    @Transactional
    List<Film> getMostPopularFilms(int limit);

    Mpa getMpaById(int id);
    List<Mpa> getAllMpa();

    Genre getGenreById(int id);

    List<Genre> getAllGenres();
}
