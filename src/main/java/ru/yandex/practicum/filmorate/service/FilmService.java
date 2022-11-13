package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmService {
    // Поставить лайк
    void setLike(int id, long userId);
    void unsetLike(int id, long userId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    void deleteAllFilms();

    Film getFilmById(int id);

    List<Film> getPopularFilmList(int count);

    Mpa getMpaById(int id);

    List<Mpa> getAllMpa();

    Genre getGenreById(int id);

    List<Genre> getAllGenres();
}
