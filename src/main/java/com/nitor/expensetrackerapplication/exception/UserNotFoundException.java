package com.nitor.ems.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String invalidCredentials) {
    }
}
