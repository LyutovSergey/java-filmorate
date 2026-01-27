package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.common.CommonDbStorage;
import java.util.Collection;
import java.util.Optional;

@Repository("filmDbStorage")
public class FilmDbStorage extends CommonDbStorage<Film>  implements FilmStorage{
    private static final String FIND_ALL_QUERY1 = """
        SELECT f.*,
            mp.name AS mpa_name,
        GROUP_CONCAT(g.id || ':' || g.name) AS genres_data
        FROM film f
        LEFT JOIN film_genre fg ON f.id = fg.film_id
        LEFT JOIN genre g ON fg.genre_id = g.id
        LEFT JOIN mpa_rating mp ON f.mpa_rating_id = mp.id
        GROUP BY f.id, mp.id""";

    private static final String FIND_ALL_QUERY = """
    SELECT
    f.*,
    mp.name AS mpa_name,
    g_agg.genres_data,
    l_agg.user_id_likes
    FROM film f
    LEFT JOIN mpa_rating mp ON f.mpa_rating_id = mp.id
-- Подзапрос для жанров
    LEFT JOIN (
            SELECT fg.film_id,
            GROUP_CONCAT(g.id || ':' || g.name) AS genres_data
    FROM film_genre fg
    JOIN genre g ON fg.genre_id = g.id
    GROUP BY fg.film_id
) g_agg ON f.id = g_agg.film_id
-- Подзапрос для лайков
    LEFT JOIN (
            SELECT fl.film_id,
            GROUP_CONCAT(fl.user_id) AS user_id_likes
    FROM film_like fl
    GROUP BY fl.film_id
) l_agg ON f.id = l_agg.film_id""";

    public FilmDbStorage(JdbcTemplate jdbc) {
        super(jdbc, new FilmDbRowMapper());
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Optional<Film> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public Collection<Film> findAll()  {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public boolean isFilmIdRegistered(Long filmId) {
        return false;
    }
}
