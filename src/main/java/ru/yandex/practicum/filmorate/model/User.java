package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class User extends BaseUnit {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\S+$")
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;

    @Builder.Default
    @JsonIgnore
    Set<Friendship> friendships = new HashSet<>();
}
