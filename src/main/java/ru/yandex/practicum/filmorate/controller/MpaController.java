package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;

import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/mpa")
public class MpaController {

    private final FilmService service;

    public MpaController(FilmServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        if (id < 1 || id > 5) {
            throw new MpaNotFoundException("MPA not found");
        }

        log.info("MPA с ID = {} найден!", id);
        return service.getMpaById(id);
    }

    @GetMapping
    public List<Mpa> getAllMpa() {

        return service.getAllMpa();
    }
}
