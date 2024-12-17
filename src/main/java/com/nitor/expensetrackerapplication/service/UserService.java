package com.nitor.expensetrackerapplication.service;

import com.nitor.expensetrackerapplication.dto.UserDto;
import com.nitor.expensetrackerapplication.model.User;

public interface UserService {

    UserDto loginUser(User user);

    UserDto registerUser(User user);
    UserDto getUserByUsername(String username);
}
