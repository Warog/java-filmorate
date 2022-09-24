package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            Film validatedFilm = validateFilmInfo(film);
            films.put(film.getId(), validatedFilm);

            log.info("Фильм добавлен");

            return films.get(validatedFilm.getId());
        }

        throw new ValidationException("Фильм с таким id уже есть");
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Film validatedFilm = validateFilmInfo(film);
        films.put(film.getId(), film);

        log.info("Фильм обновлен");

        return films.get(validatedFilm.getId());
    }

    @GetMapping
    public List<Film> allFilms() {
        log.info("Вывод всех фильмов");

        return new ArrayList<>(films.values());
    }

    @DeleteMapping
    public void deleteAllFilms() {
        films.clear();
        log.info("Все фильмы удалены");

    }

    // Валидация данных для фильма
    private Film validateFilmInfo(Film film) {
        if (
                film != null
                && film.getDescription().length() < 200
                && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0
        ) {
            log.debug("Валидация фильма прошла успешно!");
            return film;
        }

        log.debug("Фильм не прошел валидацию {}", film);

        throw new ValidationException("Для фильма указаны неверные данные!");
    }
}
