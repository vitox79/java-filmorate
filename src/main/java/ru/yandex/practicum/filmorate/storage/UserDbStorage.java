package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private int count = 0;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean userExists(int id) {

        int num = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE id = ?",
                Integer.class,
                id);

        return num > 0;
    }

    @Override
    public void deleteFriendship(User user, User friend) {

        if (friend.getFriends().contains(user)) {
            jdbcTemplate.update(
                    "UPDATE FRIENDS SET STATUS = ? WHERE user_id = ?",
                    FriendshipStatus.PENDING.toString(),
                    friend.getId()
            );
        }
        String deleteQuery = "DELETE FROM FRIENDS WHERE user_id = ? and friend_id =?";
        jdbcTemplate.update(deleteQuery, user.getId(), friend.getId());
    }

    @Override
    public void save(User user) {

        if (userExists(user.getId())) {
            jdbcTemplate.update(
                    "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?",
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId()
            );
            if (user.getFriends() != null) {
                for (int friendId : user.getFriends()) {
                    int count = jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?",
                            Integer.class,
                            user.getId(),
                            friendId
                    );
                    if (count == 0) {
                        jdbcTemplate.update(
                                "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)",
                                user.getId(),
                                friendId,
                                user.getFriendshipStatuses().get(friendId).toString()
                        );
                    } else {
                        jdbcTemplate.update(
                                "UPDATE friends SET status = ? WHERE user_id = ? AND friend_id = ?",
                                user.getFriendshipStatuses().get(friendId).toString(),
                                user.getId(),
                                friendId
                        );
                    }
                }
            }
        } else {
            count++;
            user.setId(count);
            jdbcTemplate.update(
                    "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday()
            );
        }
    }

    @Override
    public User getByID(int id) {

        try {
            Map<String, Object> userRow = jdbcTemplate.queryForMap(
                    "SELECT * FROM users WHERE id = ?",
                    id
            );
            User user = User.builder()
                    .id((int) userRow.get("id"))
                    .email((String) userRow.get("email"))
                    .login((String) userRow.get("login"))
                    .name((String) userRow.get("name"))
                    .birthday(((java.sql.Date) userRow.get("birthday")).toLocalDate())
                    .build();

            List<Map<String, Object>> friendRows = jdbcTemplate.queryForList(
                    "SELECT * FROM friends WHERE user_id = ?",
                    id
            );
            Set<Integer> friends = new HashSet<>();
            Map<Integer, FriendshipStatus> friendshipStatuses = new HashMap<>();
            for (Map<String, Object> friendRow : friendRows) {
                int friendId = (int) friendRow.get("friend_id");
                friends.add(friendId);
                FriendshipStatus status = FriendshipStatus.valueOf((String) friendRow.get("status"));
                friendshipStatuses.put(friendId, status);
            }
            user.setFriends(friends);
            user.setFriendshipStatuses(friendshipStatuses);
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public List<User> getAll() {

        List<User> users = new ArrayList<>();
        List<Map<String, Object>> userRows = jdbcTemplate.queryForList(
                "SELECT * FROM users"
        );
        for (Map<String, Object> userRow : userRows) {
            int id = (int) userRow.get("id");
            User user = getByID(id);
            users.add(user);
        }
        return users;
    }

    public int size() {

        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    }
}
