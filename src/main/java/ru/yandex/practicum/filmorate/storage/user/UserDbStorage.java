package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import static ru.yandex.practicum.filmorate.storage.SqlRequests.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public User getUser(long id) {
        User user = jdbcTemplate.queryForObject(SQL_GET_USER_BY_ID, this::mapRowToUser, id);
        List<Friend> friends = jdbcTemplate.query(SQL_GET_USER_FRIEND_LIST_BY_ID, this::mapRowToFriend, id);
        user.setFriends(friends); // TODO fix NPE

        return user;
    }

    @Override
    public User addUser(User user) {
        jdbcTemplate.update(SQL_INSERT_USER, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());

        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(SQL_UPDATE_USER, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId()); // TODO friends надо убрать

        return user;
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
