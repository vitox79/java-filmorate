package ru.yandex.practicum.filmorate.service;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String message) {
        super(message);
    }
}