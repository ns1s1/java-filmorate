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
                                .content(TestHelper.getContent("controller/request/film/film.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAMovieWithAnInvalidName() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestHelper.getContent("controller/request/film/filmFailName.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAMovieWithAnInvalidDescription() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestHelper.getContent("controller/request/film/filmFailDescription.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAMovieWithAnInvalidDuration() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestHelper.getContent("controller/request/film/filmFailDuration.json")))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAMovieWithAnInvalidReleaseDate() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestHelper.getContent("controller/request/film/filmFailReleaseDate.json")))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldCreateFilmReleaseDateEmpty() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestHelper.getContent("controller/request/film/filmReleaseDateEmpty.json")))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
}