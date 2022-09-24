package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {
    private static int userId = 0;
    private final static Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        User validatedUser = validateUser(user);
        validatedUser.setId(++userId);
        users.put(validatedUser.getId(), validatedUser);

        log.info("Пользователь добавлен");

        return validatedUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            User validatedUser = validateUser(user);
            users.put(validatedUser.getId(), validatedUser);

            log.info("Данные пользователя обновлены");

            return users.get(validatedUser.getId());
        }

        throw new ValidationException("невозможно обновить данные пользователя!");
    }

    @GetMapping
    public List<User> allUsers() {

        log.info("Вывод спсика пользвоателей");

        return new ArrayList<>(users.values());
    }

    @DeleteMapping
    public void deleteAllUsers() {
        users.clear();
        userId = 0;

        log.info("Все пользователи удалены");

    }

    // Валидация данных для пользователя
    private User validateUser(User user) {
        if (
                user.getEmail() != null && user.getEmail().contains("@") &&
                user.getLogin() != null && !user.getLogin().isBlank() && !user.getLogin().contains(" ") &&
                user.getBirthday().isBefore(LocalDate.now())
        ) {
            if (user.getName().isBlank() || user.getName() == null) {
                user.setName(user.getLogin());
            }
            log.debug("Пользователь прошел валидацию");
            return user;
        }
        log.debug("Пользователь НЕ прошел валидацию: {}", user);

        throw new ValidationException("Неверно указаны данные пользователя!");
    }
}
