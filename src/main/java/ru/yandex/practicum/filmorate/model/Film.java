package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;


@Builder
@Data
public class Film {
    @Min(0)
    private int id;
    @NotBlank(message = "Навзвание не может быть пустым!")
    @NotNull(message = "Навзвание фильма не должно быть нулевым!")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть пололжительной!")
    private int duration;
    private Set<Long> likes;
    private Genre genre;
    private Rating rating;
}

