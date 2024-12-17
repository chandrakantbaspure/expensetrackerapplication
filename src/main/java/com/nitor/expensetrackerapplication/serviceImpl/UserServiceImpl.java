package com.nitor.expensetrackerapplication.serviceImpl;

import com.nitor.expensetrackerapplication.dto.UserDto;
import com.nitor.expensetrackerapplication.exception.UserAlreadyExist;
import com.nitor.expensetrackerapplication.exception.UserInvalidException;
import com.nitor.expensetrackerapplication.model.User;
import com.nitor.expensetrackerapplication.repo.UserRepository;
import com.nitor.expensetrackerapplication.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserDto loginUser(User user) {
        log.info("Login user: {}", user);

        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return modelMapper.map(existingUser, UserDto.class);
        }
        throw new UserInvalidException("Invalid credentials");
    }

    @Override
    public UserDto registerUser(User user) {
        log.info("Register user: {}", user);
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExist("User already exists");
        }
        Random random = new Random();
        Long expenseId = random.nextLong(1000);
        user.setId(expenseId);
        String username = user.getUsername();
        user.setUsername(username);
        String password = user.getPassword();
        user.setPassword(password);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        log.info("Get user by username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return modelMapper.map(user, UserDto.class);
        }
        throw new UserInvalidException("User not found");
    }

}
