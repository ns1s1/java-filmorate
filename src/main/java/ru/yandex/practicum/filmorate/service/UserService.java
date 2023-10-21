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
        List<User> friends = new ArrayList<>();
        for(long friendId : user.getFriends()) {
            friends.add(userStorage.getUserById(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        Set<Long> user = userStorage.getUserById(id).getFriends();
        Set<Long> otherUser = userStorage.getUserById(otherId).getFriends();

        Set<Long> commonFriendsId = user.stream()
                .filter(otherUser::contains)
                .collect(Collectors.toSet());

        List<User> commonFriends = new ArrayList<>();
        for (Long userId : commonFriendsId) {
            commonFriends.add(userStorage.getUserById(userId));
        }

        return commonFriends;
    }
}
