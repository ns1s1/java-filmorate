package ru.yandex.practicum.filmorate.controller;

import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TestHelper {

    protected static String getContent(String file) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + file).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}
