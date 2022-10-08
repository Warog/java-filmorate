package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getFilm(int id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> allFilms();

    void deleteAllFilms();
}
