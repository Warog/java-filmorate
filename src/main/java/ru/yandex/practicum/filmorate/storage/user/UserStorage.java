package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getUser(long id);
    User addUser(User user);

    User updateUser(User user);

    List<User> allUsers();

    void deleteAllUsers();

    List<User> getCommonFriends(long id, long otherId);

    List<User> getFriendList(long id);

    void addFriend(long id, long friendId);

    void removeFriend(long id, long friendId);
}
