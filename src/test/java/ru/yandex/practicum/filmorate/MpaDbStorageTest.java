package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureTestDatabase
class MpaDbStorageTest {
    @Autowired
    private MpaDbStorage mpaDbStorage;

    @Test
    void shouldGetAllMpa() {
        assertEquals(5, mpaDbStorage.getAll().size());
    }

    @Test
    void shouldGetMpaById() {
        Mpa mpa = Mpa.builder()
                .id(4L)
                .name("R")
                .description("лицам до 17 лет просматривать фильм можно только в присутствии взрослого")
                .build();
        assertEquals("NC-17", mpaDbStorage.getMpaById(5L).get().getName());
        assertEquals(mpa, mpaDbStorage.getMpaById(4L).get());
    }
}
