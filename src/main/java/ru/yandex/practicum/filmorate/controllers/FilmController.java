package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController()
@RequestMapping("/films")
public class FilmController {

    private final static Map<Integer, Film> films = new HashMap<>();
    private static int filmId = 0;

    private static final int DESCRIPTION_MAX_LENGTH = 200;
    private static final LocalDate FILM_EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

            Film validatedFilm = validateFilmInfo(film);
            validatedFilm.setId(++filmId);
            films.put(validatedFilm.getId(), validatedFilm);

            log.info("Фильм добавлен");

            return films.get(validatedFilm.getId());
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            Film validatedFilm = validateFilmInfo(film);
            films.put(film.getId(), film);

            log.info("Фильм обновлен");

            return films.get(validatedFilm.getId());
        }

        throw new ValidationException("Невозможно обновить фильм! Его нет в спсике!");
    }

    @GetMapping
    public List<Film> allFilms() {
        log.info("Вывод всех фильмов");

        return new ArrayList<>(films.values());
    }

    @DeleteMapping
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
