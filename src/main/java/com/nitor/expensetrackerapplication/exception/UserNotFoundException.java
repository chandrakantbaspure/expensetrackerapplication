package com.nitor.expensetrackerapplication.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String invalidCredentials) {
    }
}
