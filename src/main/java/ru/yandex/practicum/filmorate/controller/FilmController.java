package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.Validate;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> filmsStorage = new HashMap<>();
    private long generatedId;

    @PostMapping
    public Film create(@RequestBody @Valid Film film, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        film.setId(++generatedId);
        filmsStorage.put(film.getId(), film);
        log.info("Новый фильм добавлен {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        if (filmsStorage.containsKey(film.getId())) {
            filmsStorage.put(film.getId(), film);
            log.info("Данные фильма изменены {}", film);
        } else {
            throw new ValidationException("Фильм не найден");
        }

        return film;
    }

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(filmsStorage.values());
    }
}
