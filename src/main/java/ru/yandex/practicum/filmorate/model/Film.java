package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;


@Builder
@Data
public class Film {
    private int id;
    @NotBlank(message = "Навзвание не может быть пустым!")
    @NotNull(message = "Навзвание фильма не должно быть нулевым!")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть пололжительной!")
    private int duration;
}
