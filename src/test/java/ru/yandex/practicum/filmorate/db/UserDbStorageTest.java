package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @Test
    public void testGetFilm() {

        Optional<User> filmOptional = Optional.ofNullable(userStorage.getUser(4));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 4L)
                );
    }

    @Test
    public void testAddUser() {
        User testUser = new User(
                6L,
                "Titanic@mail.com",
                "Titan",
                "Tonua",
                LocalDate.of(2000, 1, 2),
                List.of(new Friend(1, false), new Friend(2, false))
        );

        Optional<User> userOptional = Optional.ofNullable(userStorage.addUser(testUser));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 6L);
                            assertThat(user).hasFieldOrPropertyWithValue("email", "Titanic@mail.com");
                            assertThat(user).hasFieldOrPropertyWithValue("login", "Titan");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "Tonua");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(2000, 1, 2));
                            assertThat(user).hasFieldOrPropertyWithValue("friends", List.of(new Friend(1, false), new Friend(2, false)));
                        }
                );
    }

    @Test
    public void testUpdateUser() {
        User testUser = new User(
                6L,
                "Titanic@mail.com",
                "Titan",
                "Tonua",
                LocalDate.of(2000, 1, 2),
                List.of(new Friend(1, false), new Friend(2, false))
        );

        userStorage.addUser(testUser);

        User updatedUser = new User(
                6L,
                "TitanicUpdate@mail.com",
                "TitanUpdate",
                "TonuaUpdate",
                LocalDate.of(2001, 2, 3),
                List.of(new Friend(3, false), new Friend(4, false))
        );


        Optional<User> userOptional = Optional.ofNullable(userStorage.updateUser(updatedUser));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 6L);
                            assertThat(user).hasFieldOrPropertyWithValue("email", "TitanicUpdate@mail.com");
                            assertThat(user).hasFieldOrPropertyWithValue("login", "TitanUpdate");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "TonuaUpdate");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(2001, 2, 3));
                            assertThat(user).hasFieldOrPropertyWithValue("friends", List.of(new Friend(3, false), new Friend(4, false)));
                        }
                );
    }

    @Test
    public void testAllUsers() {

        Optional<List<User>> userOptional = Optional.ofNullable(userStorage.allUsers());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users.size()).isEqualTo(5);
                            assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(users.get(2)).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(users.get(3)).hasFieldOrPropertyWithValue("id", 4L);
                            assertThat(users.get(4)).hasFieldOrPropertyWithValue("id", 5L);
                        }
                );
    }

    @Test
    public void testDeleteAllUsers() {

        Optional<List<User>> userOptional = Optional.ofNullable(userStorage.allUsers());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(users -> assertThat(users.size()).isEqualTo(5)
                );

        userStorage.deleteAllUsers();

        Optional<List<User>> userAfterDelete = Optional.ofNullable(userStorage.allUsers());

        assertThat(userAfterDelete)
                .isPresent()
                .hasValueSatisfying(users -> assertThat(users.size()).isEqualTo(0)
                );
    }

    @Test
    public void testGetCommonFriends() {

        Optional<List<User>> userOptional = Optional.ofNullable(userStorage.getCommonFriends(1, 5));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users.size()).isEqualTo(1);
                            assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 3L);
                        }
                );
    }

    @Test
    public void testGetFriendList() {

        Optional<List<User>> userOptional = Optional.ofNullable(userStorage.getFriendList(5));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users.size()).isEqualTo(3);
                            assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(users.get(2)).hasFieldOrPropertyWithValue("id", 3L);
                        }
                );
    }

    @Test
    public void testAddFriend() {

        userStorage.addFriend(3L, 1L);
        Optional<List<User>> userOptional = Optional.ofNullable(userStorage.getFriendList(3L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users.size()).isEqualTo(2);
                            assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 4L);
                            assertThat(users.get(0)).hasFieldOrPropertyWithValue("friends", List.of(new Friend(1, false)));
                            assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(users.get(1)).hasFieldOrPropertyWithValue("friends", List.of(new Friend(3, true)));
                        }
                );
    }

    @Test
    public void testRemoveFriend() {
        Optional<List<User>> userOptional = Optional.ofNullable(userStorage.getFriendList(5L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users.size()).isEqualTo(3);
                            assertThat(users.get(2)).hasFieldOrPropertyWithValue("id", 3L);
                           }
                );

        userStorage.removeFriend(5L, 3L);

        Optional<List<User>> userFriendListAfterRemove = Optional.ofNullable(userStorage.getFriendList(5L));

        assertThat(userFriendListAfterRemove)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users.size()).isEqualTo(2);
                            assertThat(users.get(0)).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(users.get(1)).hasFieldOrPropertyWithValue("id", 2L);
                        }
                );
    }
}
