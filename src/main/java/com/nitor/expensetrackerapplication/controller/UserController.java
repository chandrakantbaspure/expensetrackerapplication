package com.nitor.ems.controller;

import com.nitor.ems.exception.UserAlreadyExist;
import com.nitor.ems.exception.UserInvalidException;
import com.nitor.ems.exception.UserNotFoundException;
import com.nitor.ems.model.User;
import com.nitor.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        User existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }
        User authenticatedUser = userService.authenticate(user);
        if (authenticatedUser == null || !authenticatedUser.getPassword().equals(user.getPassword()) || !authenticatedUser.getEmail().equals(user.getEmail())) {
            throw new UserInvalidException("Invalid email or password");
        }
        return ResponseEntity.ok(authenticatedUser);
    }


    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        User existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            throw new UserAlreadyExist("User with email " + user.getEmail() + " already exists");
        }
        User newUser = userService.signup(user);
        return ResponseEntity.status(201).body(newUser);
    }

    @GetMapping("/user/{emailId}")
    public ResponseEntity<User> getUserDetails(@PathVariable String emailId) {
        try {
            User user = userService.getUserByEmail(emailId);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
