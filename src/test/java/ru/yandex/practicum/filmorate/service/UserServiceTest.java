package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private UserStorage userStorage;

    @BeforeEach
    void init() {

        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        userStorage = new UserDbStorage(jdbcTemplate);
    }

    List<User> createUsers() {

        List<User> users = new ArrayList<>();
        User user1 = User.builder()
                .name("name1")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .login("login2")
                .build();
        User user2 = User.builder()
                .name("name2")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .login("login2")
                .build();
        users.add(user1);
        users.add(user2);
        users.add(user1);
        users.add(user2);
        return users;
    }

    @Test
    void addUser() {

        List<User> users = createUsers();
        userStorage.save(users.get(0));
        User user = userStorage.getByID(1);
        assertTrue(users.get(0).getLogin().equals(user.getLogin()));
        assertTrue(users.get(0).getName().equals(user.getName()));

    }

    @Test
    void getAll() {

        List<User> users = createUsers();
        userStorage.save(users.get(0));
        userStorage.save(users.get(1));

    }

    @Test
    void addFriend() {

        List<User> users = createUsers();
        userStorage.save(users.get(0));
        userStorage.save(users.get(1));

        users.get(0).setId(1);
        users.get(0).setFriends(new HashSet<>());
        users.get(0).getFriends().add(2);
        users.get(0).setFriendshipStatuses(new HashMap<>());
        users.get(0).setFriendshipStatuses(new HashMap<>());
        users.get(0).getFriendshipStatuses().put(2, FriendshipStatus.CONFIRMED);


        users.get(1).setId(2);
        users.get(1).setFriends(new HashSet<Integer>());
        users.get(1).getFriends().add(1);
        users.get(1).setFriendshipStatuses(new HashMap<>());
        users.get(1).getFriendshipStatuses().put(1, FriendshipStatus.CONFIRMED);

        userStorage.update(users.get(0));
        userStorage.update(users.get(1));
        User user1 = userStorage.getByID(1);
        User user2 = userStorage.getByID(2);


        assertTrue(user1.getFriends().contains(2));
        assertTrue(user2.getFriends().contains(1));
    }

    @Test
    void removeFriend() {

        List<User> users = createUsers();
        userStorage.save(users.get(0));
        userStorage.save(users.get(1));

        users.get(0).setId(1);
        users.get(0).setFriends(new HashSet<Integer>());
        users.get(0).getFriends().add(2);
        users.get(0).setFriendshipStatuses(new HashMap<>());
        users.get(0).getFriendshipStatuses().put(2, FriendshipStatus.CONFIRMED);


        users.get(1).setId(2);
        users.get(1).setFriends(new HashSet<Integer>());
        users.get(1).getFriends().add(1);
        users.get(1).setFriendshipStatuses(new HashMap<>());
        users.get(1).getFriendshipStatuses().put(1, FriendshipStatus.CONFIRMED);

        userStorage.update(users.get(0));
        userStorage.update(users.get(1));
        userStorage.deleteFriendship(users.get(0), users.get(1));
        userStorage.deleteFriendship(users.get(1), users.get(0));
        User user1 = userStorage.getByID(1);
        User user2 = userStorage.getByID(2);

        assertTrue(user1.getFriends().isEmpty());
        assertTrue(user2.getFriends().isEmpty());
    }
}