package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    // Поставить лайк
    boolean setLike(int id, long userId);
    boolean unsetLike(int id, long userId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    void deleteAllFilms();

    Film getFilmById(int id);

    List<Film> getPopularFilmList(int count);
}
