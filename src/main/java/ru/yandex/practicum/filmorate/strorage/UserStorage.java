package ru.yandex.practicum.filmorate.strorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void save(User user);
    User getByID(int id);
    List<User> getAll();
}

