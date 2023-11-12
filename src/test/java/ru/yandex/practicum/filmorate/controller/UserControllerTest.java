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
public class UserControllerTest {

    private static final String PATH = "/users";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void create() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestHelper.getContent("controller/request/user/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAMovieWithAnInvalidBirthday() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestHelper.getContent("controller/request/user/userFailBirthday.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAMovieWithAnInvalidEmail() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestHelper.getContent("controller/request/user/userFailEmail.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldThrowAnExceptionWhenCreatingAMovieWithAnInvalidLogin() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestHelper.getContent("controller/request/user/userFailLogin.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
