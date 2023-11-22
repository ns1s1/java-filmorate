package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureTestDatabase
class GenreDbStorageTest {
    @Autowired
    private GenreDbStorage genreDbStorage;

    @Test
    void shouldGetAllGenres() {
        assertEquals(6, genreDbStorage.getAll().size());
    }

    @Test
    void shouldGetGenreById() {
        Genre genre = Genre.builder()
                .id(6L)
                .name("Боевик")
                .build();
        assertEquals("Триллер", genreDbStorage.getGenreById(4L).get().getName());
        assertEquals(genre, genreDbStorage.getGenreById(6L).get());
    }
}