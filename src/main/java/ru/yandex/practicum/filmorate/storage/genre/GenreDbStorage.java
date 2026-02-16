package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.common.CommonDbStorage;
import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreDbStorage extends CommonDbStorage<Genre> implements GenreStorage {

    private static final String FIND_ALL_QUERY =
            "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY =
            "SELECT * FROM genre WHERE id = ?";

    public GenreDbStorage(JdbcTemplate jdbc) {
        super(jdbc, new GenreDbRowMapper());
    }

    @Override
    public Optional<Genre> getById(int id) {
        return findOneInDb(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Collection<Genre> findAll() {
        return  findManyInDb(FIND_ALL_QUERY);
    }
}
