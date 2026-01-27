package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class FilmDbRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(Optional.ofNullable(rs.getDate("release_date"))
                        .map(Date::toLocalDate)
                        .orElse(null))
                .duration(rs.getLong("duration_in_ms"))
                .mpa(new Mpa(rs.getInt("mpa_rating_id"), rs.getString("mpa_name")))
                .build();

        // Жанры
        String genres = rs.getString("genres_data");

        if (genres != null && !genres.isBlank()) {
            // Разделяем по запятой
            String[] genresArray = genres.split(",");
            for (String genreStr : genresArray) {
                // Разделяем по двоеточию (id : name)
                String[] parts = genreStr.split(":");
                Integer genreId = Integer.parseInt(parts[0]);
                String genreName = parts[1];
                film.getGenres().add(new Genre(genreId, genreName));
            }
        }

        // Лайки
        String likes = rs.getString("user_id_likes");

        if (likes != null && !likes.isBlank()) {
            Arrays.stream(likes.split(","))
                    .map(Long::valueOf)
                    .forEach(film::addUserLike);
        }
        return film;
    }
}