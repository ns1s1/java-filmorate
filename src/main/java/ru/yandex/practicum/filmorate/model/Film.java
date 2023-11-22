package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class Film extends BaseUnit {

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @MinimumDate
    private LocalDate releaseDate;

    @Min(1)
    private int duration;

    @NotNull
    private Mpa mpa;

    @JsonIgnore
    private Set<Long> likes = new HashSet<>();

    private Set<Genre> genres = new TreeSet<>();
}
