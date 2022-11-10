package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        if (id < 1) {
            throw new IncorrectParameterException("id");
        }
        if (friendId < 1) {
            throw new UserNotFoundException("User not found!");
        }

        service.addFriend(id, friendId);
        log.info("Друг с id = {} добавлен", friendId);
        return service.getUserById(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        if (id < 1) {
            throw new IncorrectParameterException("id");
        }
        if (friendId < 1) {
            throw new IncorrectParameterException("friendId");
        }

        service.deleteFriend(id, friendId);
        log.info("Друг с id = {} удален", friendId);

        return "Пользователь удален";
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@PathVariable long id) {
        if (id < 1) {
            throw new IncorrectParameterException("id");
        }

        log.info("Списко друзей пользователя с id = {} получен!", id);
        return service.showAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(@PathVariable long id, @PathVariable long otherId) {
        if (id < 1) {
            throw new IncorrectParameterException("id");
        }
        if (otherId < 1) {
            throw new IncorrectParameterException("otherId");
        }

        log.info("Вывод списка общих друзей пользователя с id = {} и пользователя с id = {} получен!", id, otherId);

        return service.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        if (id < 1) {
            throw new IncorrectParameterException("id");
        }
//        if (id > service.getAllUsers().size()) {                  TODO!!!
//            throw new UserNotFoundException("User not found!");
//        }

        log.info("Поиск пользователя с id = {}", id);
        return service.getUserById(id);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return service.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return service.updateUser(user);
    }

    @GetMapping
    public List<User> allUsers() {
        return service.getAllUsers();
    }

    @DeleteMapping
    public String deleteAllUsers() {
        service.deleteAllUsers();

        return "Все пользователи удалены";
    }
}
