package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    
    private final UserStorage storage;

    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    // Добавить друга
    public void addFriend(long id, long friendId) {
        storage.getUser(id).getFriends().add(friendId);
        storage.getUser(friendId).getFriends().add(id);
    }

    // Удалить друга
    public void deleteFriend(long id, long friendId) {
        storage.getUser(id).getFriends().remove(friendId);
        storage.getUser(friendId).getFriends().remove(id);
    }

    // Показать список друзей
    public List<User> showAllFriends(long id) {
        Set<Long> friends = storage.getUser(id).getFriends();

        return friends.stream()
                .map(this::getUserById).collect(Collectors.toList());
    }

    // Получить пользователя по ID
    public User getUserById(long id) {
        return storage.getUser(id);
    }

    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return storage.allUsers();
    }

    public void deleteAllUsers() {
        storage.deleteAllUsers();
    }

    public List<User> getCommonFriends(long id, long otherId) {

        return storage.getUser(id).getFriends().stream()
                .filter(storage.getUser(otherId).getFriends()::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
