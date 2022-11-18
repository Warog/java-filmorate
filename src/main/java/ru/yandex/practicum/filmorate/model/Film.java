package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class Film {
    @Min(0)
    private int id;
    @NotBlank(message = "Навзвание не может быть пустым или равнятся null!")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной!")
    private int duration;
    private List<Long> likes;
    //Оценка
    private Integer rate;
    // Рейтинг
    @NotNull
    private Mpa mpa;
    private List<Genre> genres;
}

