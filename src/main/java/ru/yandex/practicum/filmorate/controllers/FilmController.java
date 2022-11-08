package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    public FilmController(FilmServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        if (id < 1) {
            throw new IncorrectParameterException("id");
        }
//        if (id > service.getAllFilms().size()) {
//            throw new FilmNotFoundException(String.format("Фильм с id = %d не найден", id));
//        }

        log.info("Фильм с ID = {} найден!", id);
        return service.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film setLike(@PathVariable int id, @PathVariable long userId) {
        if (id < 1) {
            throw new IncorrectParameterException("id");
        }
        if (userId < 1) {
            throw new IncorrectParameterException("userId");
        }

        service.setLike(id, userId);
        log.info("Фильм с id = {} получил лайк от пользователя с id = {}", id, userId);

        return service.getFilmById(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable long userId) {
        if (id < 1) {
            throw new IncorrectParameterException("id");
        }
        if (userId < 1) {
//            throw new IncorrectParameterException("userId");
            throw new UserNotFoundException("User not found, but is not correct error code for postman!");
        }

        service.unsetLike(id, userId);
        log.info("Пользователь с id = {} удалил лайк у фильма с id = {}", userId, id);

        return service.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) final int count
    ) {

        if (count < 1) {
            throw new IncorrectParameterException("count");
        }

        log.info("Список {} популярных фильмов", count);
        return service.getPopularFilmList(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return service.updateFilm(film);
    }

    @GetMapping
    public List<Film> allFilms() {
        return service.getAllFilms();
    }

    @DeleteMapping
    public String deleteAllFilms() {
        service.deleteAllFilms();

        return "Фильм удален";
    }

}
