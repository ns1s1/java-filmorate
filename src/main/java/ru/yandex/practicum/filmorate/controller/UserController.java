package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Validate;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> usersStorage = new HashMap<>();
    private long generatedId;

    @PostMapping
    public User create(@RequestBody @Valid User user, BindingResult bindingResult) {
        Validate.validate(bindingResult);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++generatedId);
        usersStorage.put(user.getId(), user);
        log.info("Новый пользователь добавлен {}", user);

        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        if (usersStorage.containsKey(user.getId())) {
            usersStorage.put(user.getId(), user);
            log.info("Данные фильма изменены {}", user);
        } else {
            throw new ValidationException("Пользователь не найден");
        }

        return user;
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(usersStorage.values());
    }
}



