package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public class UserDbStorage implements UserStorage {
    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public List<User> allUsers() {
        return null;
    }

    @Override
    public void deleteAllUsers() {

    }
}
