package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.common.CommonDbStorage;
import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaDbStorage extends CommonDbStorage<Mpa> implements MpaStorage {

    private static final String FIND_ALL_QUERY =
            "SELECT * FROM mpa_rating ";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM mpa_rating  WHERE id = ?";

    public MpaDbStorage(JdbcTemplate jdbc) {
        super(jdbc, new MpaDbRowMapper());
    }

    @Override
    public Optional<Mpa> getById(int id) {
        return findOneInDb(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Collection<Mpa> findAll() {
        return  findManyInDb(FIND_ALL_QUERY);
    }
}
