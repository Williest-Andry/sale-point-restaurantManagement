package com.williest.td2springbootrestaurant.service.exception;

public class ServerException extends RuntimeException{
    public ServerException(String message) {
        super(message);
    }
}
