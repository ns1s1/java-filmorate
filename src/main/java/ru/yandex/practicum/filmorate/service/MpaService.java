package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@Service
public class MpaService {

    @Autowired
    private final MpaDbStorage mpaDbStorage;

    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> findAll() {
        return mpaDbStorage.getAll();
    }

    public Mpa getMpaById(Long mpaId) {
        return mpaDbStorage.getMpaById(mpaId)
                .orElseThrow(() -> new NotFoundException("Mpa не найден"));
    }
}
