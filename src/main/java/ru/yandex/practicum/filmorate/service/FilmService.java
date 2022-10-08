package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    // Поставить лайк
    public boolean setLike(int id, long userId) {

        return filmStorage.getFilm(id).getLikes().add(userId);
    }

    // Удалить лайк
    public boolean unsetLike(int id, long userId) {
        return getFilmById(id).getLikes().remove(userId);
    }


    public Film addFilm(Film film) {
       return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
       return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.allFilms();
    }

    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilm(id);
    }

    public List<Film> getPopularFilmList(int count) {
        return filmStorage.allFilms().stream()
                .sorted(Comparator.comparingLong(film -> film.getLikes().size() * -1))
                .limit(count)
                .collect(Collectors.toList());
    }
}

