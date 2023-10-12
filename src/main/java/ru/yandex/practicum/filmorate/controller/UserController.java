package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Validate;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    @PostMapping
    public User create(@RequestBody @Valid User user, BindingResult bindingResult) {
        Validate.validate(bindingResult);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User createUser = super.create(user);
        log.info("Новый пользователь добавлен {}", createUser);

        return createUser;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        User updetedUser = super.update(user);
        log.info("Данные пользователя изменены {}", updetedUser);

        return updetedUser;
    }

    @GetMapping
    public List<User> findAll() {
        return super.findAll();
    }
}



