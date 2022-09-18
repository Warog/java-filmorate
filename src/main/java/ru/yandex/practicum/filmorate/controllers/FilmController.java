package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/films")
public class FilmController {

    private final static List<Film> films = new ArrayList<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        films.add(validateFilmInfo(film));

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {


        return film;
    }

    @GetMapping
    public List<Film> allFilms() {

        return films;
    }

    // Валидация данных для фильма
    private Film validateFilmInfo(Film film) {
        if (
                film != null
                && film.getDescription().length() < 200
                && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0
        ) {
            return film;
        }

        throw new ValidationException("Для фильма указаны неверные данные!");
    }
}
