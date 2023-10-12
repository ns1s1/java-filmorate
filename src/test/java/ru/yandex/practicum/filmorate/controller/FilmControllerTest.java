package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.NestedServletException;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FilmControllerTest {

    private static final String PATH = "/films";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void create() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFile("controller/request/film/film.json")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateFilmFailName() throws Exception {
        NestedServletException exception = assertThrows(
                NestedServletException.class,
                () -> mockMvc.perform(
                                MockMvcRequestBuilders.post(PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(getContentFromFile("controller/request/film/filmFailName.json")))
                        .andExpect(MockMvcResultMatchers.status().is5xxServerError()));

        assertEquals("name - must not be blank", Objects.requireNonNull(
                exception.getMessage()).substring(108));
    }

    @Test
    void shouldCreateFilmFailDescription() throws Exception {
        NestedServletException exception = assertThrows(
                NestedServletException.class,
                () -> mockMvc.perform(
                                MockMvcRequestBuilders.post(PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(getContentFromFile("controller/request/film/filmFailDescription.json")))
                        .andExpect(MockMvcResultMatchers.status().is5xxServerError()));

        assertEquals("description - size must be between 0 and 200", Objects.requireNonNull(
                exception.getMessage()).substring(108));
    }

    @Test
    void shouldCreateFilmFailDuration() throws Exception {
        NestedServletException exception = assertThrows(
                NestedServletException.class,
                () -> mockMvc.perform(
                                MockMvcRequestBuilders.post(PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(getContentFromFile("controller/request/film/filmFailDuration.json")))
                        .andExpectAll(MockMvcResultMatchers.status().is5xxServerError()));

        assertEquals("duration - must be greater than or equal to 1", Objects.requireNonNull(
                exception.getMessage()).substring(108));
    }

    @Test
    void shouldCreateFilmFailReleaseDate() throws Exception {
        final NestedServletException exception = assertThrows(
                NestedServletException.class,
                () -> mockMvc.perform(
                                MockMvcRequestBuilders.post(PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(getContentFromFile("controller/request/film/filmFailReleaseDate.json")))
                        .andExpectAll(status().is5xxServerError()));

        assertEquals("releaseDate - Фильм должен быть позже 1895-12-28", Objects.requireNonNull(
                exception.getMessage()).substring(108));
    }

    @Test
    void shouldCreateFilmReleaseDateEmpty() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFile("controller/request/film/filmReleaseDateEmpty.json")))
                .andExpect(status().isBadRequest());
    }

    private String getContentFromFile(String file) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + file).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}