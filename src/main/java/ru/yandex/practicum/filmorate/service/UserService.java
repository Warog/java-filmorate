package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    List<User> showAllFriends(long id);

    User getUserById(long id);

    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    void deleteAllUsers();

    List<User> getCommonFriends(long id, long otherId);
}
