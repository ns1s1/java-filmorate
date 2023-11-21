package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private final GenreDbStorage genreDbStorage;

    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genre> findAll() {
        return genreDbStorage.getAll();
    }

    public Genre getGenreById(Long genreId) {
        return genreDbStorage.getGenreById(genreId)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }
}
