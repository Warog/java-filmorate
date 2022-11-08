package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

@Builder
@Data
public class User {

    private Long id;
    @NotNull(message = "Почта не может быть пустой")
    @Email(message = "Неверный формат e-mail")
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;
//    private status ;
}
