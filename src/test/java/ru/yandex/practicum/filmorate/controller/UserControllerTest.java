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
                                .content(getContent("controller/request/user/user.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldCreateUserFailBirthday() throws Exception {
        NestedServletException exception = assertThrows(
                NestedServletException.class,
                () -> mockMvc.perform(
                                MockMvcRequestBuilders.post(PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(getContent("controller/request/user/userFailBirthday.json")))
                        .andExpect(MockMvcResultMatchers.status().is5xxServerError()));

        assertEquals("birthday - must be a date in the past or in the present", Objects.requireNonNull(
                exception.getMessage().substring(108)));
    }

    @Test
    void shouldCreateUserFailEmail() throws Exception {
        NestedServletException exception = assertThrows(
                NestedServletException.class,
                () -> mockMvc.perform(
                                MockMvcRequestBuilders.post(PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(getContent("controller/request/user/userFailEmail.json")))
                        .andExpect(MockMvcResultMatchers.status().is5xxServerError()));

        assertEquals("email - must be a well-formed email address", Objects.requireNonNull(
                exception.getMessage().substring(108)));
    }

    @Test
    void shouldCreateUserFailLogin() throws Exception {
        NestedServletException exception = assertThrows(
                NestedServletException.class,
                () -> mockMvc.perform(
                                MockMvcRequestBuilders.post(PATH)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(getContent("controller/request/user/userFailLogin.json")))
                        .andExpect(MockMvcResultMatchers.status().is5xxServerError()));

        assertEquals("birthday - must be a date in the past or in the present", Objects.requireNonNull(
                exception.getMessage().substring(108)));
    }

    private String getContent(String file) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + file).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}