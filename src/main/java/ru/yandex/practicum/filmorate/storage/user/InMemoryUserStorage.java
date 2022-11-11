package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        User validatedUser = UserValidator.validateUserValues(user);
        validatedUser.setId(++userId);
//        validatedUser.setFriends(new HashSet<>());
        users.put(validatedUser.getId(), validatedUser);

        log.info("Пользователь добавлен");

        return validatedUser;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            User validatedUser = UserValidator.validateUserValues(user);
            if (user.getFriends() == null) {
//                validatedUser.setFriends(new HashSet<>());
            } else {
                validatedUser.setFriends(UserValidator.validateUserValues(user).getFriends());
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
}
