package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BaseController<T extends BaseUnit> {

    protected final Map<Long, T> storage = new HashMap<>();
    private long generatedId;

    public T create(T data) {
        data.setId(++generatedId);
        storage.put(data.getId(), data);

        return data;
    }

    public T update(T data) {
        if (storage.containsKey(data.getId())) {
            storage.put(data.getId(), data);
        } else {
            throw new ValidationException(String.format("%s не найден", data));
        }

        return data;
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }
}
