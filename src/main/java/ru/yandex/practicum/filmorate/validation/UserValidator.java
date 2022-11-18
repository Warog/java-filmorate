package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.SqlRequests;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
public class UserValidator {

    // Валидация данных для пользователя
    public static User validateUserValues(User user) {
        if (
            user.getEmail() != null && user.getEmail().contains("@") &&
            user.getLogin() != null && !user.getLogin().isBlank() && !user.getLogin().contains(" ") &&
            user.getBirthday().isBefore(LocalDate.now())
        ) {
            Optional<String> userName = Optional.ofNullable(user.getName());
            if (userName.isEmpty() || userName.get().isBlank()) {
                user.setName(user.getLogin());
            }
            log.debug("Пользователь прошел валидацию данных");
            return user;
        }
        log.debug("Пользователь НЕ прошел валидацию данных: {}", user);

        throw new ValidationException("Неверно указаны данные пользователя!");
    }

    public static Boolean validateUserExistsInDB(JdbcTemplate jdbcTemplate, User user) {
        Optional<Boolean> userIsExist = Optional.ofNullable(jdbcTemplate.queryForObject(SqlRequests.SQL_FIND_USER_ID_BOOLEAN, Boolean.class, user.getId()));
        if (userIsExist.isPresent()) {
            if (userIsExist.get()){
                log.debug("Пользователь c ID = {} найден в БД", user.getId());

                return true;
            } else {
                log.debug("Пользователь c ID = {} НЕ найден в БД", user.getId());

                return false;
            }

        } else {
            log.debug("Пользователь НЕ прошел валидацию: {}", user);

            throw new ValidationException("Ошибка при обращении к БД!");
        }
    }
}
