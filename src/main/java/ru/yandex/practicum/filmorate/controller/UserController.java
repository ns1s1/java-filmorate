package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable(value = "id") long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable(value = "id") long id,
                                       @PathVariable(value = "otherId") long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable(value = "id") long id,
                           @PathVariable(value = "friendId") long friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriends(@PathVariable(value = "id") long id,
                              @PathVariable(value = "friendId") long friendId) {
        userService.removeFriends(id, friendId);
    }
}



