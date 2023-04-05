package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private UserDbStorage userStorage;
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {

        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        userStorage = new UserDbStorage(jdbcTemplate);
        addUsers();

    }

    @AfterEach
    void shutDown() {

        embeddedDatabase.shutdown();
    }

    void addUsers() {

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
        userStorage.save(user1);
        userStorage.save(user2);

    }



    @Test
    public void testFindUserById() {

        Optional<User> userOptional = Optional.ofNullable(userStorage.getByID(1));
        Optional<User> userOptional2 = Optional.ofNullable(userStorage.getByID(2));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
        assertThat(userOptional2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id",2 )
                );

        assertThat(userOptional2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "login2")
                );
        assertThat(userOptional2)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(2002, 12, 12))
                );


    }
}