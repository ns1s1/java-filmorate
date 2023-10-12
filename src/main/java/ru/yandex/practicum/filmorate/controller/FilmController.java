package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.Validate;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends BaseController<Film> {

    @PostMapping
    public Film create(@RequestBody @Valid Film film, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        Film createFilm = super.create(film);
        log.info("Новый фильм добавлен {}", createFilm);
        return createFilm;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film, BindingResult bindingResult) {
        Validate.validate(bindingResult);
        Film updatedFilm = super.update(film);
        log.info("Данные фильма изменены {}", updatedFilm);

        return updatedFilm;
    }

    @GetMapping
    public List<Film> findAll() {
        return super.findAll();
    }
}
