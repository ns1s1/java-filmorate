package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> findAll();

    void create(User user);

    void update(User user);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);
}