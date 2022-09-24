package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Data
public class User {
    @Min(0)
    private int id;
    @NotNull(message = "Почта не может быть пустой")
    @Email(message = "Неверный формат e-mail")
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
