package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    void create(Film film);

    void update(Film film);

    Optional<Film> getFilmById(Long id);

    List<Film> getAllFilms();

    void removeFilmById(Long id);
}
