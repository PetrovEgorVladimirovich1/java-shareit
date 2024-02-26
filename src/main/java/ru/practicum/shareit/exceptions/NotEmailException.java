package ru.practicum.shareit.exceptions;

public class NotEmailException extends RuntimeException {
    public NotEmailException(String message) {
        super(message);
    }
}
