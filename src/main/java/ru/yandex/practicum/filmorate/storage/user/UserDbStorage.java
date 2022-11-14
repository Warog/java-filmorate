package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.FriendValidator;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.SqlRequests.*;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public User getUser(long id) {
        try {
            Optional<User> userOfDb = Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_USER_BY_ID, this::mapRowToUser, id));
            List<Friend> friends = jdbcTemplate.query(SQL_GET_USER_FRIEND_LIST_BY_ID, this::mapRowToFriend, id);

            if (userOfDb.isPresent()) {
                userOfDb.get().setFriends(friends);

                return userOfDb.get();
            } else {
                return null;
            }


        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь с ID = " + id + " не найден в БД");
        }
    }

    @Transactional
    @Override
    public User addUser(User user) {
        User validatedUser = UserValidator.validateUserValues(user);
        jdbcTemplate.update(SQL_INSERT_USER, validatedUser.getEmail(), validatedUser.getLogin(), validatedUser.getName(), validatedUser.getBirthday());

        Long maxId = jdbcTemplate.queryForObject(SQL_GET_MAX_USER_ID, Long.class);
        validatedUser.setId(maxId);

        log.info("Пользователь добавлен: {}", validatedUser);
        return validatedUser;
    }

    @Transactional
    @Override
    public User updateUser(User user) {
        User validatedUser = UserValidator.validateUserValues(user);
        if (UserValidator.validateUserExistsInDB(jdbcTemplate, validatedUser)) {
            jdbcTemplate.update(SQL_UPDATE_USER, validatedUser.getEmail(), validatedUser.getLogin(), validatedUser.getName(), validatedUser.getBirthday(), validatedUser.getId()); // TODO friends надо убрать
        } else {
            throw new UserNotFoundException("Невозможно обновить данные пользователя!");
        }

        return validatedUser;
    }

    @Transactional
    @Override
    public List<User> allUsers() {

        List<User> userList = jdbcTemplate.query(SQL_GET_ALL_USERS, this::mapRowToUser);

        for (User user : userList) {
            List<Friend> friends = jdbcTemplate.query(SQL_GET_USER_FRIEND_LIST_BY_ID, this::mapRowToFriend, user.getId());
            user.setFriends(friends);
        }

        return userList;
    }

    @Override
    public void deleteAllUsers() {
        jdbcTemplate.update(SQL_DELETE_ALL_USERS);
    }

    @Transactional
    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        List<Long> commonFriendsIds = jdbcTemplate.queryForList(SQL_GET_COMMON_FRIENDS_IDs, Long.class, id, otherId);

        return commonFriendsIds.stream().map(this::getUser).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<User> getFriendList(long id) {
        List<Long> friendsIDs = jdbcTemplate.query(SQL_GET_USER_FRIEND_LIST_BY_ID, (rs, rowNum) -> rs.getLong("FRIEND_ID"), id);

        return friendsIDs.stream().map(this::getUser).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void addFriend(long id, long friendId) {
        try {
            if (FriendValidator.isFriendship(jdbcTemplate, id, friendId)) {
                jdbcTemplate.update(SQL_UPDATE_FRIENDSHIP_STATUS, true, friendId, id);
                jdbcTemplate.update(SQL_INSERT_FRIEND, id, friendId, true);
            } else {
                jdbcTemplate.update(SQL_INSERT_FRIEND, id, friendId, false);
            }
        } catch (DuplicateKeyException e) {
            throw new ValidationException("Такая запись уже существует");
        }


    }

    @Override
    public void removeFriend(long id, long friendId) {
        if (FriendValidator.isFriendship(jdbcTemplate, id, friendId)) {
            jdbcTemplate.update(SQL_UPDATE_FRIENDSHIP_STATUS, false, friendId, id);
            jdbcTemplate.update(SQL_DELETE_FRIEND, id, friendId);
        } else {
            jdbcTemplate.update(SQL_DELETE_FRIEND, id, friendId);
        }
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    private Friend mapRowToFriend(ResultSet resultSet, int rowNum) throws SQLException {
        return Friend.builder()
                .friendId(resultSet.getInt("friend_id"))
                .status(resultSet.getBoolean("status"))
                .build();
    }
}
