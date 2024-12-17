package com.nitor.expensetrackerapplication.serviceImpl;

import com.nitor.expensetrackerapplication.dto.UserDto;
import com.nitor.expensetrackerapplication.exception.UserAlreadyExist;
import com.nitor.expensetrackerapplication.exception.UserInvalidException;
import com.nitor.expensetrackerapplication.exception.UserNotFoundException;
import com.nitor.expensetrackerapplication.model.User;
import com.nitor.expensetrackerapplication.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, modelMapper);
    }

    @Test
    void loginUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("testPassword");
        User map = modelMapper.map(userDto, User.class);
        when(userRepository.findByUsername("testUser")).thenReturn(map);
        UserDto userResult = userService.loginUser(map);
        assertEquals("testUser", userResult.getUsername());
        assertEquals("testPassword", userResult.getPassword());
    }

    @Test
    void registerUser_success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.registerUser(user);

        assertEquals("testUser", result.getUsername());
        assertEquals("testPassword", result.getPassword());
    }

    @Test
    void registerUser_fail_duplicate_user() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("testPassword");
        User map = modelMapper.map(userDto, User.class);
        when(userRepository.findByUsername("testUser")).thenReturn(map);

        assertThrows(UserAlreadyExist.class, () -> userService.registerUser(map));
    }

    @Test
    void getUserByUsername() {
        UserDto userDto = new UserDto();
        userDto.setUsername("testUser");
        userDto.setPassword("testPassword");
        User map = modelMapper.map(userDto, User.class);
        when(userRepository.findByUsername("testUser")).thenReturn(map);
        UserDto userResult = userService.getUserByUsername("testUser");
        assertEquals("testUser", userResult.getUsername());
        assertEquals("testPassword", userResult.getPassword());
    }
    @Test
    void getUserByUsername_not_found() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);
        assertThrows(UserInvalidException.class, () -> userService.getUserByUsername("testUser"));
    }
    @Test
    void getUserByUsername_null() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.getUserByUsername("testUser"));
    }
    @Test
    void getUserByUsername_empty() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.getUserByUsername("testUser"));
    }
    @Test
    void getUserByUsername_null_empty() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> userService.getUserByUsername("testUser"));
    }
}