package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.common.CommonDbStorage;

import java.util.*;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage extends CommonDbStorage<User> implements UserStorage {

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
    private static final String IS_EMAIL_REGISTERED_QUERY =
            "SELECT EXISTS(SELECT 1 FROM user_app WHERE email = ?)";
    private static final String IS_USER_ID_REGISTERED_QUERY =
            "SELECT EXISTS(SELECT 1 FROM user_app WHERE id = ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE user_app SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friend (user_id, friend_user_id, is_confirmed)" +
    "VALUES (?, ?, ?)";
    private static final String
            DEL_FRIEND_QUERY = "DELETE FROM friend WHERE user_id = ? AND friend_user_id = ?";
    private static final String ADD_USER_QUERY = "INSERT INTO user_app (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String GET_FRIENDS_QUERY = """
        SELECT uf.*, GROUP_CONCAT(f.friend_user_id || ':' || f.is_confirmed) AS friends_data
        FROM (
                SELECT * FROM user_app u
                WHERE u.id in ( SELECT friend_user_id FROM friend WHERE user_id = ?)
             ) as uf
        LEFT JOIN friend f ON uf.id = f.user_id
        GROUP BY uf.id""";

    private static final String GET_COMMON_FRIENDS_QUERY = """
        SELECT uf.*, GROUP_CONCAT(f.friend_user_id || ':' || f.is_confirmed) AS friends_data
        FROM (
                SELECT u.*
                FROM user_app u
                JOIN friend f1 ON u.id = f1.friend_user_id
                JOIN friend f2 ON u.id = f2.friend_user_id
                WHERE f1.user_id = ? AND f2.user_id = ?
              ) as uf
        LEFT JOIN friend f ON uf.id = f.user_id
        GROUP BY uf.id""";

    public UserDbStorage(JdbcTemplate jdbc) {
        super(jdbc, new UserDbRowMapper());
    }

    @Override
    @Transactional
    public User create(User user) {
        long id = insertInDb(ADD_USER_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return findOneInDb(FIND_BY_ID_QUERY, id).orElse(null);
    }

    @Override
    @Transactional
    public User update(User user) {
        updateInDb(UPDATE_USER_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return findOneInDb(FIND_BY_ID_QUERY, user.getId()).get();
    }

    @Override
    public Optional<User> getById(Long id) {
        return findOneInDb(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Collection<User> findAll() {
        return findManyInDb(FIND_ALL_QUERY);
    }

    @Override
    public boolean isEmailRegistered(String email) {
        return isExistsInDb(IS_EMAIL_REGISTERED_QUERY, email);
    }

    @Override
    public boolean isUserIdRegistered(Long userId) {
        return isExistsInDb(IS_USER_ID_REGISTERED_QUERY, userId);
    }

    @Override
    public void removeFriend(Long userId, Long friendUserId) {
        deleteInDb(DEL_FRIEND_QUERY, userId, friendUserId);
    }

    @Override
    public void addFriend(Long userId, Long friendUserId) {
        updateInDb(ADD_FRIEND_QUERY, userId, friendUserId, true); // Функционал подтверждения пока не реализован
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long otherUserId) {
        return findManyInDb(GET_COMMON_FRIENDS_QUERY, userId, otherUserId);
    }

    @Override
    public Collection<User> getFriends(Long userId) {
        return findManyInDb(GET_FRIENDS_QUERY, userId);
    }
}
