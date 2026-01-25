package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.common.CommonDbStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage extends CommonDbStorage<User> implements UserStorage {

   // private static final String FIND_ALL_QUERY = "SELECT * FROM user_app";
   // private static final String FIND_BY_ID_QUERY = "SELECT * FROM user_app where id = ?";
    private static final String FIND_ALL_QUERY =
            "SELECT u.*, GROUP_CONCAT(f.friend_user_id || ':' || f.is_confirmed) AS friends_data " +
                    "FROM user_app u " +
                    "LEFT JOIN friend f ON u.id = f.user_id " +
                    "GROUP BY u.id;";

    private static final String FIND_ALL_QUERY1 = """
        SELECT u.id, u.login, u.name, u.email, u.birthday, 
               LISTAGG(f.friend_user_id || ':' || f.is_confirmed, ',') AS friends_data
        FROM user_app u
        LEFT JOIN friend f ON u.id = f.user_id
        GROUP BY u.id, u.login, u.name, u.email, u.birthday
        """;


    private static final String FIND_BY_ID_QUERY =
            "SELECT u.*, GROUP_CONCAT(f.friend_user_id || ':' || f.is_confirmed) AS friends_data " +
                    "FROM user_app u " +
                    "LEFT JOIN friend f ON u.id = f.user_id " +
                    "WHERE u.id = ? " +
                    "GROUP BY u.id";
    private static final String UPDATE_USER_QUERY = "UPDATE user_app SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String FIND_USER_BY_EMAIL_QUERY =
            "SELECT u.*, GROUP_CONCAT(f.friend_user_id || ':' || f.is_confirmed) AS friends_data " +
                    "FROM user_app u " +
                    "LEFT JOIN friend f ON u.id = f.user_id " +
                    "WHERE u.email = ? " +
                    "GROUP BY u.id";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friend (user_id, friend_user_id, is_confirmed)" +
    "VALUES (?, ?, ?)";
    private static final String DEL_FRIEND_QUERY = "DELETE FROM friend WHERE user_id = ? AND friend_user_id = ?";
    private static final String ADD_USER_QUERY ="INSERT INTO user_app (email, login, name, birthday) VALUES (?, ?, ?, ?)";


    public UserDbStorage(JdbcTemplate jdbc) {
        super(jdbc, new UserDbRowMapper());
    }

    @Override
    public User create(User user) {
        long id= insert(ADD_USER_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        updateFriends( user, new User());
        return findOne(FIND_BY_ID_QUERY, id).orElse(null);
    }

    @Override
    public User update(User user) {

        User oldUser = findOne(FIND_BY_ID_QUERY, user.getId()).get();

        update(UPDATE_USER_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        updateFriends(user,oldUser);
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
        return findOne(FIND_USER_BY_EMAIL_QUERY, email).isPresent();
    }

    @Override
    public boolean isUserIdRegistered(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId).isPresent();
    }

     private void updateFriends(User user, User oldUser) {
        // Добавление новых друзей
        Set<Long> addFriends = new HashSet<>(user.getFriendsId());
        addFriends.removeAll(oldUser.getFriendsId());
        for (Long idFriend : addFriends) {
            update(ADD_FRIEND_QUERY, user.getId(), idFriend, true); // считаем подтвержденной по умолчанию
        }

        // Удаление отсутствующих друзей
        Set<Long> delFriends = new HashSet<>(oldUser.getFriendsId());
        delFriends.removeAll(user.getFriendsId());
        for (Long idFriend : delFriends) {
            update(DEL_FRIEND_QUERY, user.getId(), idFriend);
        }
    }


}
