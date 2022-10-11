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
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    // Поставить лайк
    @Override
    public boolean setLike(int id, long userId) {

        return filmStorage.getFilm(id).getLikes().add(userId);
    }

    // Удалить лайк
    @Override
    public boolean unsetLike(int id, long userId) {
        return getFilmById(id).getLikes().remove(userId);
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
        return filmStorage.allFilms().stream()
                .sorted(Comparator.comparingLong(film -> film.getLikes().size() * -1))
                .limit(count)
                .collect(Collectors.toList());
    }
}

