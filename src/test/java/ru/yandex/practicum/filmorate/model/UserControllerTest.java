package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.strorage.UserStorage;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    @Autowired
    UserStorage repository;

    @Test
    void emptyName(){
        ValidateService validateService = new ValidateService();
        User user = User.builder()
                .name(" ")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .login("login")
                .build();
        user.setBirthday(LocalDate.of(2000,10,10));
        validateService.validateUser(user);
        assertEquals(user.getLogin(),user.getName());
    }
    @Test
    void addUser(){
        User user = User.builder()
                .name("name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .login("login")
                .build();
        repository.save(user);
        assertEquals(repository.getByID(user.getId()),user);
    }
    @Test
    void getUsers(){
        User user = User.builder()
                .name("name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .login("login")
                .build();
        repository.save(user);
        User user2 = User.builder()
                .name("name2")
                .email("mail2@mail.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .login("login2")
                .build();
        user2.setId(2);
        user2.setName("name2");
        user2.setEmail("mail2@mail.ru");
        user2.setLogin("login2");
        user2.setBirthday(LocalDate.of(2000,10,10));
        repository.save(user2);
        assertEquals(repository.getAll().size(),2);
        assertEquals(repository.getByID(2),user2);
    }


}