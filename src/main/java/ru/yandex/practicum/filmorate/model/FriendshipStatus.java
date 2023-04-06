package ru.yandex.practicum.filmorate.model;

public enum FriendshipStatus {
    NONE,
    PENDING, // запрос на добавление в друзья отправлен, но не подтвержден
    CONFIRMED // друзья
}