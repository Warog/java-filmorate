package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final static Map<Integer, Film> films = new HashMap<>();
    private static int filmId = 0;
    private static final int DESCRIPTION_MAX_LENGTH = 200;
    private static final LocalDate FILM_EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);


    @Override
    public Film getFilm(int id) {
            return films.get(id);

    }
    @Override
    public Film addFilm(Film film) {
        Film validatedFilm = validateFilmInfo(film);
        validatedFilm.setId(++filmId);
        validatedFilm.setLikes(new HashSet<>());
        films.put(validatedFilm.getId(), validatedFilm);

        log.info("Фильм добавлен");

        return films.get(validatedFilm.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            Film validatedFilm = validateFilmInfo(film);
            if (film.getLikes() == null) {
                validatedFilm.setLikes(new HashSet<>());
            } else {
                validatedFilm.setLikes(validateFilmInfo(film).getLikes());
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
    public void deleteAllFilms() {
        films.clear();
        filmId = 0;
        log.info("Все фильмы удалены");
    }

    // Валидация данных для фильма
    private Film validateFilmInfo(Film film) {
        if (
                film != null
                        && film.getDescription().length() < DESCRIPTION_MAX_LENGTH
                        && film.getReleaseDate().isAfter(FILM_EARLIEST_RELEASE_DATE)
                        && film.getDuration() > 0
        ) {
            log.debug("Валидация фильма прошла успешно!");
            return film;
        }

        log.debug("Фильм не прошел валидацию {}", film);

        throw new ValidationException("Для фильма указаны неверные данные!");
    }
}
