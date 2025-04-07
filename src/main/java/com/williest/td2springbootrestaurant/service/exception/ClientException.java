package com.williest.td2springbootrestaurant.service.exception;

public class ClientException extends RuntimeException {
    public ClientException(String message) {
        super(message);
    }
}
