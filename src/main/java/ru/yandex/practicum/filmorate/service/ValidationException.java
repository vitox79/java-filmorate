package ru.yandex.practicum.filmorate.service;

public class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
}

