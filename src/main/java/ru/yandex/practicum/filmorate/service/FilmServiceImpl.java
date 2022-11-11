package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    // Поставить лайк
    @Override
    public void setLike(int id, long userId) {
        filmStorage.setLike(id, userId);
    }

    // Удалить лайк
    @Override
    public void unsetLike(int id, long userId) {
        filmStorage.unsetLike(id, userId);
    }

    @Override
    public Film addFilm(Film film) {
       return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
       return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.allFilms();
    }

    @Override
    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }

    @Override
    public Film getFilmById(int id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public List<Film> getPopularFilmList(int count) {
        return filmStorage.getMostPopularFilms(count);
    }
}

