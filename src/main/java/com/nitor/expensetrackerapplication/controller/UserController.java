package com.nitor.expensetrackerapplication.controller;

import com.nitor.expensetrackerapplication.dto.UserDto;
import com.nitor.expensetrackerapplication.exception.UserInvalidException;
import com.nitor.expensetrackerapplication.exception.UserNotFoundException;
import com.nitor.expensetrackerapplication.model.User;
import com.nitor.expensetrackerapplication.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody User user) {
        log.info("Login request received for user: {}", user.getUsername());
        UserDto existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }
        UserDto authenticatedUser = userService.loginUser(user);
        if (authenticatedUser == null || !authenticatedUser.getPassword().equals(user.getPassword()) || !authenticatedUser.getUsername().equals(user.getUsername())) {
            throw new UserInvalidException("Invalid email or password");
        }
        return ResponseEntity.ok(authenticatedUser);
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody User user) {
        log.info("Registration request received for user: {}", user.getUsername());
        UserDto newUser = userService.registerUser(user);
        return ResponseEntity.status(201).body(newUser);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable String username) {
        try {
            UserDto user = userService.getUserByUsername(username);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
