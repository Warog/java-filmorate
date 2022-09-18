package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @NotNull(message = "Почта не может быть пустой")
    @Email(message = "Неверный формат e-mail")
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
