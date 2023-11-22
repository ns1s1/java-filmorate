package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Genre extends BaseUnit implements Comparable<Genre> {

    private String name;

    @Override
    public int compareTo(Genre genre) {
        return getId().compareTo(genre.getId());
    }
}
