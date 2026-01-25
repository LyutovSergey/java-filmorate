package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.common.CommonDbStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage extends CommonDbStorage<User> implements UserStorage {

   // private static final String FIND_ALL_QUERY = "SELECT * FROM user_app";
   // private static final String FIND_BY_ID_QUERY = "SELECT * FROM user_app where id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT u.*, GROUP_CONCAT(f.friend_user_id || ':' || f.is_confirmed) AS friends_data " +
                    "FROM user_app u " +
                    "LEFT JOIN friend f ON u.id = f.user_id " +
                    "GROUP BY u.id";

    private static final String FIND_BY_ID_QUERY =
            "SELECT u.*, GROUP_CONCAT(f.friend_user_id || ':' || f.is_confirmed) AS friends_data " +
                    "FROM user_app u " +
                    "LEFT JOIN friend f ON u.id = f.user_id " +
                    "WHERE u.id = ? " +
                    "GROUP BY u.id";
    private static final String UPDATE_QUERY = "UPDATE user_app SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    public UserDbStorage(JdbcTemplate jdbc) {
        super(jdbc, new UserDbRowMapper());
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        update(UPDATE_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return findOne(FIND_BY_ID_QUERY, user.getId()).get();
    }

    @Override
    public Optional<User> getById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Collection<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public boolean isEmailRegistered(String email) {
        return false;
    }

    @Override
    public boolean isUserIdRegistered(Long userId) {
        return false;
    }
}
