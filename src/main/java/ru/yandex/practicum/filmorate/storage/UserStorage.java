package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void deleteFriendship(User user, User friend);

    void save(User user);

    void update(User user);

    User getByID(int id);

    List<User> getAll();
}

