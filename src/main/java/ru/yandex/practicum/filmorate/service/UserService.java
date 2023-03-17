package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.strorage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserStorage users;

    public void addUser(User user) {
        user.setFriends(new HashSet<>());
        users.save(user);
    }

    public User getByID(int id) {
        return users.getByID(id);
    }

    public List<User> getAll() {
        return users.getAll();
    }

    public void addFriend(User user, User friend) {
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    public void removeFriend(User user, User friend) {
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public List<User> getCommonFriends(User user1, User user2) {
        List<User> friends = new ArrayList<>();
        if ((user1.getFriends() == null) || (user2.getFriends() == null)) {
            return friends;
        }

        for (Integer id : user1.getFriends()) {
            if (user2.getFriends().contains(id)) {
                User friend = users.getByID(id);
                friends.add(friend);
            }
        }
        return friends;
    }

    public List<User> getFriends(User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        List<User> friends = new ArrayList<>();
        for (Integer id : user.getFriends().stream().collect(Collectors.toList())) {
            friends.add(users.getByID(id));
        }
        return friends;
    }


}
