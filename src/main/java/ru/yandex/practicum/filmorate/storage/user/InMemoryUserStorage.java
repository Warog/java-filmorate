package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private static long userId = 0;
    private final static Map<Long, User> users = new HashMap<>();

    @Override
    public User getUser(long id) {
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        User validatedUser = validateUser(user);
        validatedUser.setId(++userId);
//        validatedUser.setFriends(new HashSet<>());
        users.put(validatedUser.getId(), validatedUser);

        log.info("Пользователь добавлен");

        return validatedUser;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            User validatedUser = validateUser(user);
            if (user.getFriends() == null) {
//                validatedUser.setFriends(new HashSet<>());
            } else {
                validatedUser.setFriends(validateUser(user).getFriends());
            }
            users.put(validatedUser.getId(), validatedUser);

            log.info("Данные пользователя обновлены");

            return users.get(validatedUser.getId());
        }

        throw new UserNotFoundException("Невозможно обновить данные пользователя!");
    }

    @Override
    public List<User> allUsers() {
        log.info("Вывод спсика пользвоателей");

        return new ArrayList<>(users.values());
    }

    @Override
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
            Optional<String> userName = Optional.ofNullable(user.getName());
            if (userName.isEmpty() || userName.get().isBlank()) {
                user.setName(user.getLogin());
            }
            log.debug("Пользователь прошел валидацию");
            return user;
        }
        log.debug("Пользователь НЕ прошел валидацию: {}", user);

        throw new ValidationException("Неверно указаны данные пользователя!");
    }
}
