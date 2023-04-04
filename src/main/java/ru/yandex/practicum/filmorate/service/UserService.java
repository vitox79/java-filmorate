package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.strorage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    @Qualifier("UserDbStorage")
    private UserStorage users;

    public void addUser(User user) {

        user.setFriends(new HashSet<>());
        users.save(user);
    }

    public void updateUser(User user) {

        users.save(user);
    }


    public User getByID(int id) {

        return users.getByID(id);
    }

    public List<User> getAll() {

        return users.getAll();
    }

    public void addFriend(User user, User friend) {

        Set<Integer> userFriends = user.getFriends();
        if (userFriends == null) {
            userFriends = new HashSet<>();
            user.setFriendshipStatuses(new HashMap<>());
            user.setFriends(userFriends);
        }
        Set<Integer> friendFriends = friend.getFriends();
        if (friendFriends != null) {
            if (friendFriends.contains(user)) {
                user.getFriendshipStatuses().put(friend.getId(), FriendshipStatus.CONFIRMED);
                friend.getFriendshipStatuses().put(user.getId(), FriendshipStatus.CONFIRMED);
                users.save(friend);
            } else {
                user.getFriendshipStatuses().put(friend.getId(), FriendshipStatus.PENDING);
            }
        }
        user.getFriends().add(friend.getId());
        users.save(user);
    }

    public void removeFriend(User user, User friend) {

        if ((user.getFriends() == null) || (friend.getFriends() == null)) {
            return;
        }
        user.getFriends().remove(friend.getId());
        user.getFriendshipStatuses().remove(friend.getId());
        if (friend.getFriendshipStatuses() != null) {
            if (friend.getFriendshipStatuses().containsKey(user.getId())) {
                friend.getFriendshipStatuses().put(user.getId(), FriendshipStatus.PENDING);
                users.save(friend);
            }
        }
        users.deleteFriendship(user, friend);
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
