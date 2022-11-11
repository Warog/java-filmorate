package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Mpa {
    private Integer id;
    @JsonIgnore
    private String name;
}
