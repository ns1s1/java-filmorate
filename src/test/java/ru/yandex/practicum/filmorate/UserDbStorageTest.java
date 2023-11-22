package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
        User newUser =  User.builder()
                .id(1L)
                .email("user1@email.ru")
                .name("user1")
                .login("user1")
                .birthday(LocalDate.of(1994, 2, 1))
                .build();

        User newUser2 =  User.builder()
                .id(2L)
                .email("user2@email.ru")
                .name("user2")
                .login("user2")
                .birthday(LocalDate.of(1991, 6, 2))
                .build();

        User newUser3 =  User.builder()
                .id(3L)
                .email("user3@email.ru")
                .name("user3")
                .login("user3")
                .birthday(LocalDate.of(1995, 12, 23))
                .build();



    @Test
    public void shouldFindUserById() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);

        User savedUser = userStorage.findById(1L).get();

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void shouldFindAll() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        userStorage.create(newUser3);
        userStorage.create(newUser2);

        List<User> users = userStorage.findAll();
        System.out.println(users);
        assertEquals(3, users.size());
    }

    @Test
    public void shouldCreateUser() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);

        User getUser = userStorage.findById(1L).get();
        Assertions.assertEquals(newUser, getUser);
    }

    @Test
    public void shouldUpdateUser() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);

        newUser.setLogin("updateUser");
        userStorage.update(newUser);

        User getUser = userStorage.findById(1L).get();
        String login = newUser.getLogin();
        Assertions.assertEquals(login, "updateUser");
        Assertions.assertEquals(newUser, getUser);
    }
}


