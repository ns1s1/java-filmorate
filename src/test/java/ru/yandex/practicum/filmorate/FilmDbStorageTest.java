package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    Mpa mpa = Mpa.builder().id(4L).name("R")
            .description("лицам до 17 лет просматривать фильм можно только в присутствии взрослого").build();
    Mpa mpa2 = Mpa.builder().id(3L).name("PG-13").description("детям до 13 лет просмотр не желателен").build();

    Genre genre = Genre.builder().id(2L).name("Драма").build();
    Genre genre1 = Genre.builder().id(1L).name("Комедия").build();

    Film film = Film.builder()
            .id(1L)
            .name("Опенгеймер")
            .description("История жизни американского физика-теоретика Роберта Оппенгеймера, " +
                    "который во времена Второй мировой войны руководил Манхэттенским проектом — " +
                    "секретными разработками ядерного оружия.")
            .releaseDate(LocalDate.of(2023, 7, 19))
            .duration(180)
            .mpa(mpa)
            .likes(new HashSet<>())
            .genres(Set.of(genre))
            .build();

    Film film1 = Film.builder()
            .id(2L)
            .name("Форрест Гамп")
            .description("С самого малолетства парень страдал от заболевания ног, соседские мальчишки дразнили его, " +
                    "но в один прекрасный день Форрест открыл в себе невероятные способности к бегу. ")
            .releaseDate(LocalDate.of(1994, 6, 23))
            .duration(142)
            .mpa(mpa2)
            .likes(new HashSet<>())
            .genres(Set.of(genre1))
            .build();

    @Test
    void shouldCreateFilm() {
        filmStorage.create(film);
        film = filmStorage.getFilmById(1L).get();
        assertThat(film)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void shouldGetAllFilms() {
        filmStorage.create(film);
        filmStorage.create(film1);

        List<Film> getFilms = filmStorage.getAllFilms();

        film.setMpa(mpa);
        film1.setMpa(mpa2);
        film1.setGenres(Set.of(genre1));
        film.setGenres(Set.of(genre));

        Assertions.assertEquals(film, getFilms.get(0));
        Assertions.assertEquals(film1, getFilms.get(1));
    }

    @Test
    public void shouldUpdateFilm() {
        filmStorage.create(film);

        film.setName("test");
        film.setLikes(new HashSet<>());
        filmStorage.update(film);
        Film getFilm = filmStorage.getFilmById(1L).get();
        film.setMpa(mpa);
        film.setGenres(Set.of(genre));

        Assertions.assertEquals(film, getFilm);
    }

    @Test
    public void shouldGetFilmById() {
        filmStorage.create(film);

        Film savedFilm = filmStorage.getFilmById(1L).get();

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }
}


