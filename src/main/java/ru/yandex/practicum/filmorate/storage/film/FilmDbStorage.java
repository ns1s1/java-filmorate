package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("databaseFilmStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreStorage genreDbStorage;

    @Transactional
    @Override
    public void create(Film film) {
        String query = "insert into film (film_id, name, description, mpa_id, release_date, duration) " +
                "values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDuration());

        addGenre(film, film.getGenres());
    }

    @Transactional
    @Override
    public void update(Film film) {
        String query = "update film " +
                "set name = ?," +
                "    description = ?," +
                "    mpa_id = ?," +
                "    release_date = ?," +
                "    duration = ?" +
                "where film_id = ?";

        jdbcTemplate.update(query,
                film.getName(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId());

        // Update Genres
        Set<Long> newGenreIds = film.getGenres()
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        List<Long> existGenreIds = findGenresIdsByFilmId(film.getId());

        List<Long> genreIdsToAdd = new ArrayList<>(newGenreIds);
        genreIdsToAdd.removeAll(existGenreIds);
        addGenres(film, genreIdsToAdd);

        List<Long> genreIdsToRemove = new ArrayList<>(existGenreIds);
        genreIdsToRemove.removeAll(newGenreIds);
        removeGenres(film, genreIdsToRemove);

        // Update likes
        Set<Long> newUserIds = film.getLikes();
        List<Long> existUserIds = findLikesOfUsersByFilmId(film.getId());
        List<Long> userIdsToAdd = new ArrayList<>(newUserIds);
        userIdsToAdd.remove(existUserIds);
        addLikes(film, userIdsToAdd);

        List<Long> likesToRemove = new ArrayList<>(existUserIds);
        existUserIds.remove(newUserIds);
        removeLikes(film, likesToRemove);
    }

    @Transactional
    @Override
    public void removeFilmById(Long id) {
        jdbcTemplate.update("delete from film_genre where film_id = ?", id);
        jdbcTemplate.update("delete from film where film_id = ?", id);
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return jdbcTemplate.query(
                "select film_id, name, description, mpa_id, release_date, duration from film where film_id = ?",
                this::createFilm, id).stream().findAny();
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query(
                "select film_id, name, description, mpa_id, release_date, duration from film",
                this::createFilm);
    }

    private Film createFilm(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Long mpaId = rs.getLong("mpa_id");
        Mpa mpa = mpaDbStorage.getMpaById(mpaId).orElse(null);
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer duration = rs.getInt("duration");

        Film film = Film.builder()
                .id(filmId)
                .name(name)
                .description(description)
                .mpa(mpa)
                .duration(duration)
                .releaseDate(releaseDate)
                .build();

        film.setGenres(findGenresByFilmId(film.getId()));
        film.setLikes(new HashSet<>(findLikesOfUsersByFilmId(film.getId())));
        return film;
    }

    private void addGenre(Film film, Set<Genre> genres) {
        String query = "insert into film_genre (film_id, genre_id) values (?, ?)";
        genres.forEach(genre -> jdbcTemplate.update(query, film.getId(), genre.getId()));
    }

    private void addGenres(Film film, List<Long> genreIds) {
        String query = "insert into film_genre (film_id, genre_id) values (?, ?)";
        genreIds.forEach(genreId -> jdbcTemplate.update(query, film.getId(), genreId));
    }

    private void removeGenres(Film film, List<Long> genreIds) {
        String query = "delete from film_genre where film_id = ? and genre_id = ?";
        genreIds.forEach(genreId -> jdbcTemplate.update(query, film.getId(), genreId));
    }

    private List<Long> findGenresIdsByFilmId(Long filmId) {
        return jdbcTemplate.query("select genre_id from film_genre where film_id = ?",
                (rs, rowNum) -> rs.getLong("genre_id"),
                filmId);
    }

    private Set<Genre> findGenresByFilmId(Long filmId) {
        return findGenresIdsByFilmId(filmId).stream()
                .map(genreDbStorage::getGenreById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private List<Long> findLikesOfUsersByFilmId(Long filmId) {
        return jdbcTemplate.query("select user_id from film_like where film_id = ?",
                (rs, rowNum) -> rs.getLong("user_id"),
                filmId);
    }

    private void addLikes(Film film, List<Long> userIds) {
        String query = "insert into film_like (film_id, user_id) values (?, ?)";
        userIds.forEach(userId -> jdbcTemplate.update(query, film.getId(), userId));
    }

    private void removeLikes(Film film, List<Long> userIds) {
        String query = "delete from film_like where film_id = ? and user_id = ?";
        userIds.forEach(userId -> jdbcTemplate.update(query, film.getId(), userId));
    }
}
