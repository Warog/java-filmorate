package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/users")
public class UserController {
    private final static Map<String, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        User validUser = validateUser(user);
        users.put(validUser.getEmail(), validUser);

        return validUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        users.put(user.getEmail(), user);

        return user;
    }

    @GetMapping
    public List<User> allUsers() {

        return new ArrayList<>(users.values());
    }

    // Валидация данных для пользователя
    private User validateUser(User user) {
        if (
                user.getEmail() != null && user.getEmail().contains("@") &&
                !user.getLogin().isBlank() && !user.getLogin().contains(" ") &&
                user.getBirthday().isBefore(LocalDate.now())
        ) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            return user;
        }

        throw new ValidationException("Неверно указаны данные пользователя!");
    }
}
