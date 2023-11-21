package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Primary
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void create(User user) {
        String query = "insert into users (user_id, email, login, name, birthday) " +
                "values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(query,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        user.getFriendships().forEach(friendship -> addFriendship(user, friendship));
    }

    @Transactional
    @Override
    public void update(User user) {
        String query = "update users " +
                "set email = ?," +
                "    login = ?," +
                "    name = ?," +
                "    birthday = ?" +
                "where user_id = ?";

        jdbcTemplate.update(query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        Set<Friendship> newFriendships = user.getFriendships();
        Set<Friendship> existFriendships = findFriendshipsByUserId(user.getId());


        Set<Friendship> toRemoveFriendships = new HashSet<>(existFriendships);
        toRemoveFriendships.removeAll(newFriendships);
        toRemoveFriendships.forEach(f -> removeFriendship(user, f));

        Set<Friendship> toUpdateFriendships = new HashSet<>(existFriendships);
        toUpdateFriendships.retainAll(newFriendships);
        Map<Long, Boolean> newConfirmationsStatuses = newFriendships.stream()
                .collect(Collectors.toMap(Friendship::getFriendId, Friendship::isConfirmed));
        toUpdateFriendships.stream()
                .filter(f -> f.isConfirmed() != newConfirmationsStatuses.get(f.getFriendId()))
                .forEach(f -> updateFriendship(user, f));

        Set<Friendship> toAddFriendships = new HashSet<>(newFriendships);
        toAddFriendships.removeAll(existFriendships);
        toAddFriendships.forEach(f -> addFriendship(user, f));
    }

    public void removeById(Long id) {
        jdbcTemplate.update("delete from film_genre where film_id = ?", id);
        jdbcTemplate.update("delete from film where film_id = ?", id);
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcTemplate.query(
                "select user_id, email, login, name, birthday from users where user_id = ?",
                this::createUser, id).stream().findAny();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbcTemplate.query(
                "select user_id, email, login, name, birthday from users where email = ?",
                this::createUser, email).stream().findAny();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "select user_id, email, login, name, birthday from users",
                this::createUser);
    }

    private User createUser(ResultSet rs, int rowNum) throws SQLException {
        Long userId = rs.getLong("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        User user = User.builder()
                .id(userId)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();

        user.setFriendships(findFriendshipsByUserId(userId));
        return user;
    }

    private Friendship createFriendship(ResultSet rs, int rowNum) throws SQLException {
        long friendId = rs.getLong("friend_id");
        boolean confirmed = rs.getBoolean("confirmed");
        return Friendship.builder()
                .friendId(friendId)
                .confirmed(confirmed)
                .build();
    }

    private Set<Friendship> findFriendshipsByUserId(Long userId) {
        return new HashSet<>(jdbcTemplate.query("select friend_id, confirmed from user_friend where user_id = ?",
                this::createFriendship,
                userId));
    }

    private void addFriendship(User user, Friendship friendship) {
        String query = "insert into user_friend (user_id, friend_id, confirmed) values (?, ?, ?)";
        jdbcTemplate.update(query, user.getId(), friendship.getFriendId(), friendship.isConfirmed());
    }

    private void updateFriendship(User user, Friendship friendship) {
        String query = "update user_friend set confirmed = ? where user_id = ? and friend_id = ?";
        jdbcTemplate.update(query, user.getId(), friendship.getFriendId(), friendship.isConfirmed());
    }

    private void removeFriendship(User user, Friendship friendship) {
        String query = "delete from user_friend where user_id = ? and friend_id = ?";
        jdbcTemplate.update(query, user.getId(), friendship.getFriendId());
    }
}