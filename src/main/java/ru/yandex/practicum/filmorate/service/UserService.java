package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private Long id = 0L;

    public User create(User user) {
        validate(user);
        generateId();
        user.setId(id);
        ensureName(user);
        userStorage.create(user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    public User update(User user) {
        Long id = user.getId();
        User savedUser = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        validate(user);
        ensureName(user);
        BeanUtils.copyProperties(user, savedUser, "friendships");
        userStorage.update(savedUser);
        log.info("Обновлен пользователь {}", savedUser);
        return savedUser;
    }

    public List<User> getAll() {
        return userStorage.findAll();
    }

    public User getById(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public List<User> getFriendsByUserId(Long userId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return user.getFriendships()
                .stream()
                .map(Friendship::getFriendId)
                .map(userStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.getFriendships().add(Friendship.builder().friendId(friendId).build());
        userStorage.update(user);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        userStorage.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user.getFriendships().remove(Friendship.builder().friendId(friendId).build());
        userStorage.update(user);
    }

    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        User firstUser = userStorage.findById(firstUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        User secondUser = userStorage.findById(secondUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Set<Long> commonIds = firstUser.getFriendships().stream()
                .map(Friendship::getFriendId).collect(Collectors.toSet());
        commonIds.retainAll(secondUser.getFriendships().stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet()));
        return commonIds.stream()
                .map(userStorage::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private void ensureName(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void validate(User user) {
        // Validate email
        Optional<User> foundUser = userStorage.findByEmail(user.getEmail());

        if (foundUser.isPresent() && !Objects.equals(foundUser.get().getId(), user.getId())) {
            throw new NotFoundException("Пользователь уже существует");
        }
    }

    private Long generateId() {
        return ++id;
    }
}