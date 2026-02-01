package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.common.CommonDbStorage;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository("filmDbStorage")
public class FilmDbStorage extends CommonDbStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_QUERY = """
                SELECT
                   f.*,
                   mp.name AS mpa_name,
                   g_agg.genres_data,
                   l_agg.user_id_likes
               FROM film f
               LEFT JOIN mpa_rating mp ON f.mpa_rating_id = mp.id
               -- Жанры фильмов
               LEFT JOIN (
                   SELECT
                       fg.film_id,
                       GROUP_CONCAT(g.id || ':' || g.name ORDER BY g.id ASC SEPARATOR ',') AS genres_data
                   FROM film_genre fg
                   JOIN genre g ON fg.genre_id = g.id
                   GROUP BY fg.film_id
               ) g_agg ON f.id = g_agg.film_id
               -- Лайки фильмов
               LEFT JOIN (
                   SELECT
                       fl.film_id,
                       GROUP_CONCAT(fl.user_id) AS user_id_likes
                   FROM film_like fl
                   GROUP BY fl.film_id
               ) l_agg ON f.id = l_agg.film_id
""";
    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE f.id = ?";
    private static final String ADD_FILM_QUERY =  """
            INSERT INTO film (name, description, release_date, duration_in_ms, mpa_rating_id)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_FILM_QUERY = """
            UPDATE film
            SET name = ?, description = ?, release_date = ?, duration_in_ms = ?, mpa_rating_id = ?
            WHERE id = ?
            """;
    private static final String DEL_LIKES_QUERY = "DELETE FROM film_like WHERE film_id = ? AND user_id = ?";
    private static final String ADD_LIKE_QUERY = "INSERT INTO film_like (film_id, user_id) VALUES (?, ?)";
    private static final String DEL_GENRES_QUERY = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String ADD_GENRE_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String GET_POPULAR_FILM_QUERY = """
            SELECT
                f.*,
                mp.name AS mpa_name,
                g_agg.genres_data,
                l_agg.user_id_likes
            FROM (
                -- ID популярных фильмов
                SELECT f_base.id
                FROM film f_base
                LEFT JOIN film_like fl ON f_base.id = fl.film_id
                GROUP BY f_base.id
                ORDER BY COUNT(fl.user_id) DESC, f_base.id ASC
                LIMIT ?
            ) AS top_films
            JOIN film f ON top_films.id = f.id
            LEFT JOIN mpa_rating mp ON f.mpa_rating_id = mp.id
            -- Жанры фильмов
            LEFT JOIN (
                SELECT
                    fg.film_id,
                    GROUP_CONCAT(g.id || ':' || g.name ORDER BY g.id ASC SEPARATOR ',') AS genres_data
                FROM film_genre fg
                JOIN genre g ON fg.genre_id = g.id
                GROUP BY fg.film_id
            ) g_agg ON f.id = g_agg.film_id
            -- Лайки фильмов
            LEFT JOIN (
                SELECT
                    fl.film_id,
                    GROUP_CONCAT(fl.user_id) AS user_id_likes
                FROM film_like fl
                GROUP BY fl.film_id
            ) l_agg ON f.id = l_agg.film_id
            ORDER BY (SELECT COUNT(*) FROM film_like WHERE film_id = f.id) DESC, f.id ASC;
            """;
    private static final String IS_FILM_ID_REGISTERED_QUERY =
            "SELECT EXISTS(SELECT 1 FROM film WHERE id = ?)";

    public FilmDbStorage(JdbcTemplate jdbc) {
        super(jdbc, new FilmDbRowMapper());
    }

    @Transactional
    @Override
    public Film create(Film film) {
        long id = insertInDb(ADD_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        updateGenres(film);
        return findOneInDb(FIND_BY_ID_QUERY, id).get();
    }

    @Transactional
    @Override
    public Film update(Film film) {
        updateInDb(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        updateGenres(film);
        return findOneInDb(FIND_BY_ID_QUERY, film.getId()).get();
    }

    @Override
    public Optional<Film> getById(Long id) {
        return findOneInDb(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Collection<Film> findAll()  {
        return findManyInDb(FIND_ALL_QUERY);
    }

    @Override
    public boolean isFilmIdRegistered(Long filmId) {
        return isExistsInDb(IS_FILM_ID_REGISTERED_QUERY, filmId);
    }

    @Override
    public void likeFilm(Long filmId, Long userId) {
        updateInDb(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void unlikeFilm(Long filmId, Long userId) {
        deleteInDb(DEL_LIKES_QUERY, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return findManyInDb(GET_POPULAR_FILM_QUERY, count);
    }

    private void updateGenres(Film film) {
        deleteInDb(DEL_GENRES_QUERY, film.getId());
        for (Genre genre : film.getGenres()) {
            updateInDb(ADD_GENRE_QUERY, film.getId(), genre.getId());
        }
    }
}
