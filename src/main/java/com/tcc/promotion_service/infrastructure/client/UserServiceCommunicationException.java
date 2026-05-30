package com.tcc.promotion_service.infrastructure.client;

public class UserServiceCommunicationException extends RuntimeException {
    public UserServiceCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
