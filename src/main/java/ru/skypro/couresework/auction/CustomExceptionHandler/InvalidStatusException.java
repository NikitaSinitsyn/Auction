package ru.skypro.couresework.auction.CustomExceptionHandler;

public class InvalidStatusException extends RuntimeException{
    public InvalidStatusException(String message) {
        super(message);
    }
}
