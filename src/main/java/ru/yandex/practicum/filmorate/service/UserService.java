package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void addFriends(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriends(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getAllFriends(long id) {
        User user = userStorage.getUserById(id);

        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {
        Set<Long> users = userStorage.getUserById(id).getFriends();
        Set<Long> otherUsers = userStorage.getUserById(otherId).getFriends();

        Set<Long> commonFriendsId = users.stream()
                .filter(otherUsers::contains)
                .collect(Collectors.toSet());

        List<User> commonFriendsIds = new ArrayList<>();
        for (Long userId : commonFriendsId) {
            commonFriendsIds.add(userStorage.getUserById(userId));
        }

        return commonFriendsIds;
    }
}
