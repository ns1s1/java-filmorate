package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "select mpa_id, name, description from mpa";
        return jdbcTemplate.query(sqlQuery, this::createMpa);
    }

    @Override
    public Optional<Mpa> getMpaById(Long id) {
        String sqlQuery = "select mpa_id, name, description from mpa where mpa_id = ?";
        return jdbcTemplate.query(sqlQuery, this::createMpa, id).stream().findAny();

    }

    private Mpa createMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .build();
    }
}
