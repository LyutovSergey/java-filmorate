package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDbRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        User user = User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(Optional.ofNullable(rs.getDate("birthday"))
                        .map(Date::toLocalDate)
                        .orElse(null))
                .build();

        String friendsData = rs.getString("friends_data");

        if (friendsData != null && !friendsData.isBlank()) {
            // Разделяем по запятой (разные друзья)
            String[] friendsArray = friendsData.split(",");
            for (String friendStr : friendsArray) {
                // Разделяем по двоеточию (id : status)
                String[] parts = friendStr.split(":");
                Long friendId = Long.parseLong(parts[0]);
                // Boolean confirmed = Boolean.parseBoolean(parts[1]); // Статус, если нужен в модели
                user.addFriend(friendId);
            }
        }
        return user;
    }
}