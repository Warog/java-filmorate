package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final static Map<Integer, Film> films = new HashMap<>();
    private static int filmId = 0;

    @Override
    public Film getFilm(int id) {
            return films.get(id);

    }
    @Override
    public Film addFilm(Film film) {
        Film validatedFilm = FilmValidator.validateFilmInfo(film);
        validatedFilm.setId(++filmId);
//        validatedFilm.setLikes(new HashSet<>());
        films.put(validatedFilm.getId(), validatedFilm);

        log.info("Фильм добавлен");

        return films.get(validatedFilm.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            Film validatedFilm = FilmValidator.validateFilmInfo(film);
            if (film.getLikes() == null) {
//                validatedFilm.setLikes(new HashSet<>());
            } else {
                validatedFilm.setLikes(FilmValidator.validateFilmInfo(film).getLikes());
            }

            films.put(validatedFilm.getId(), validatedFilm);

            log.info("Фильм обновлен");

            return films.get(validatedFilm.getId());
        }

        throw new FilmNotFoundException("Невозможно обновить фильм! Его нет в спсике!");
    }

    @Override
    public List<Film> allFilms() {
        log.info("Вывод всех фильмов");

        return new ArrayList<>(films.values());
    }

    @Override
    public void setLike(int filmId, long userId) {

    }

    @Override
    public void unsetLike(int filmId, long userId) {

    }

    @Override
    public void deleteAllFilms() {
        films.clear();
        filmId = 0;
        log.info("Все фильмы удалены");
    }

    @Override
    public List<Film> getMostPopularFilms(int limit) {
        return null;
    }
}
