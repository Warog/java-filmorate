package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    
    private final UserStorage storage;

    public UserServiceImpl(@Qualifier("userDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    // Добавить друга
    @Override
    public void addFriend(long id, long friendId) {
        storage.addFriend(id, friendId);
//        storage.getUser(friendId).getFriends().add(id);
    }

    // Удалить друга
    @Override
    public void deleteFriend(long id, long friendId) {
        storage.removeFriend(id, friendId);
//        storage.getUser(friendId).getFriends().remove(id);
    }

    // Показать список
    @Override
    public List<User> showAllFriends(long id) {
        return storage.getFriendList(id);

    }

    // Получить пользователя по ID
    @Override
    public User getUserById(long id) {
        return storage.getUser(id);
    }

    @Override
    public User addUser(User user) {
        return storage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return storage.allUsers();
    }

    @Override
    public void deleteAllUsers() {
        storage.deleteAllUsers();
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {

        return storage.getCommonFriends(id, otherId);

//        return storage.getUser(id).getFriends().stream()
//                .filter(storage.getUser(otherId).getFriends()::contains)
//                .map(this::getUserById)
//                .collect(Collectors.toList());

    }
}
