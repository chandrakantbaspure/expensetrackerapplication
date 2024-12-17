package com.nitor.ems.serviceImpl;

import com.nitor.ems.exception.UserAlreadyExist;
import com.nitor.ems.exception.UserInvalidException;
import com.nitor.ems.exception.UserNotFoundException;
import com.nitor.ems.model.User;
import com.nitor.ems.repository.UserRepository;
import com.nitor.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authenticate(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return existingUser;
        }
        throw new UserInvalidException("Invalid credentials");
    }

    @Override
    public User signup(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExist("User already exists");
        }
        String password = user.getPassword();
        user.setPassword(password);
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
