package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.FriendValidator;
import ru.yandex.practicum.filmorate.validation.UserValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.SqlRequests.*;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public User getUser(long id) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_GET_USER_BY_ID, this::mapRowToUser, id);
            List<Friend> friends = jdbcTemplate.query(SQL_GET_USER_FRIEND_LIST_BY_ID, this::mapRowToFriend, id);
            user.setFriends(friends); // TODO fix NPE

            return user;
        }catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь с ID = " + id + " не найден в БД");
        }
    }

    @Override
    @Transactional
    public User addUser(User user) {
        User validatedUser = UserValidator.validateUserValues(user);
        jdbcTemplate.update(SQL_INSERT_USER, validatedUser.getEmail(), validatedUser.getLogin(), validatedUser.getName(), validatedUser.getBirthday());

        Long maxId = jdbcTemplate.queryForObject(SQL_GET_MAX_USER_ID, Long.class);
        validatedUser.setId(maxId);

        log.info("Пользователь добавлен: {}", validatedUser);
        return validatedUser;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        User validatedUser = UserValidator.validateUserValues(user);
        if (UserValidator.validateUserExistsInDB(jdbcTemplate, validatedUser)) {
            jdbcTemplate.update(SQL_UPDATE_USER, validatedUser.getEmail(), validatedUser.getLogin(), validatedUser.getName(), validatedUser.getBirthday(), validatedUser.getId()); // TODO friends надо убрать
        } else {
            throw new UserNotFoundException("Невозможно обновить данные пользователя!");
        }

        return validatedUser;
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public List<User> getCommonFriends(long id, long otherId) {
        List<Long> commonFriendsIds = jdbcTemplate.queryForList(SQL_GET_COMMON_FRIENDS_IDs, Long.class, id, otherId);
        List<User> commonFriends = commonFriendsIds.stream().map(this::getUser).collect(Collectors.toList());

        return commonFriends;
    }

    @Override
    @Transactional
    public List<User> getFriendList(long id) {
        List<Long> friendsIDs = jdbcTemplate.query(SQL_GET_USER_FRIEND_LIST_BY_ID, (rs, rowNum) -> rs.getLong("FRIEND_ID"), id);
        List<User> friends = friendsIDs.stream().map(this::getUser).collect(Collectors.toList());

        return friends;
    }

    @Override
    @Transactional
    public void addFriend(long id, long friendId) {
        try {
            if(FriendValidator.isFriendship(jdbcTemplate, id, friendId)) {
                jdbcTemplate.update(SQL_UPDATE_FRIENDSHIP_STATUS, true, friendId, id);
                jdbcTemplate.update(SQL_INSERT_FRIEND, id, friendId, true);
            } else {
                jdbcTemplate.update(SQL_INSERT_FRIEND, id, friendId, false);
            }
        }catch(DuplicateKeyException e) {
            throw new ValidationException("Такая запись уже существует");
        }


    }

    @Override
    public void removeFriend(long id, long friendId) {
        if(FriendValidator.isFriendship(jdbcTemplate, id, friendId)) {
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
