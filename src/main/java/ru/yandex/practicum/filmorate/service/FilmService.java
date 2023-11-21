package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private Long id = 0L;

    public Film create(Film film) {
        validate(film);

        generateId();
        film.setId(id);
        filmStorage.create(film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    public Film update(Film film) {
        validate(film);

        Long id = film.getId();
        Film savedFilm = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id не найден"));

        savedFilm.setName(film.getName());
        savedFilm.setDescription(film.getDescription());
        savedFilm.setReleaseDate(film.getReleaseDate());
        savedFilm.setDuration(film.getDuration());
        savedFilm.setMpa(film.getMpa());
        savedFilm.setGenres(new TreeSet<>(film.getGenres()));
        filmStorage.update(savedFilm);
        log.info("Обновлен фильм {}", savedFilm);
        return savedFilm;
    }

    public List<Film> getAll() {
        return filmStorage.getAllFilms();
    }

    public Film getById(Long filmId) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        if (userService.getById(userId) == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        film.getLikes().add(userId);
        filmStorage.update(film);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));

        if (userService.getById(userId) == null) {
            throw new NotFoundException("Пользователь с не найден");
        }

        film.getLikes().remove(userId);
        filmStorage.update(film);
    }

    public List<Film> getPopular(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    public Genre getGenreById(Long genreId) {
        return genreStorage.getGenreById(genreId)
                .orElseThrow(() -> new NotFoundException("Жанр с id = %d не найден"));
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }

    public Mpa getMpaById(Long mpaId) {
        return mpaStorage.getMpaById(mpaId)
                .orElseThrow(() -> new NotFoundException("MPA не найден"));
    }

    private void validate(Film film) {
        // Validate MPA
        mpaStorage.getMpaById(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("MPA не найден"));

        // Validate Genres
        List<Genre> allGenres = genreStorage.getAll();
        String invalidGenreIds = film.getGenres().stream()
                .filter(Predicate.not(allGenres::contains))
                .map(Genre::getId)
                .map(Object::toString)
                .collect(Collectors.joining(","));

        if (!invalidGenreIds.isBlank()) {
            throw new NotFoundException("MPA не найдены");
        }
    }

    private Long generateId() {
        return ++id;
    }
}
