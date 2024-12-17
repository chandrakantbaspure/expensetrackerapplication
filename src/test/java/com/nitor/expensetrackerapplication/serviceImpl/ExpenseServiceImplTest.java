package com.nitor.expensetrackerapplication.serviceImpl;

import com.nitor.expensetrackerapplication.dto.ExpenseDto;
import com.nitor.expensetrackerapplication.exception.ExpenseNotFoundException;
import com.nitor.expensetrackerapplication.exception.UserNotFoundException;
import com.nitor.expensetrackerapplication.model.Expense;
import com.nitor.expensetrackerapplication.model.User;
import com.nitor.expensetrackerapplication.repo.ExpenseRepository;
import com.nitor.expensetrackerapplication.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseServiceImplTest {
    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private ExpenseRepository expenseRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        expenseRepository = mock(ExpenseRepository.class);
        userRepository = mock(UserRepository.class);
        modelMapper = new ModelMapper();
        expenseService = new ExpenseServiceImpl(expenseRepository, modelMapper, userRepository);
    }

    @Test
    void getAllExpenses() {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(100.0);
        expenseDto.setDate(LocalDate.parse("2023-07-10"));
        expenseDto.setCategory("Food");
        expenseDto.setTitle("Test Expense");
        expenseDto.setUserName("testuser");
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setId(1L);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(modelMapper.map(expenseDto, Expense.class));
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(expenseRepository.findExpenseByUser_Username("testuser")).thenReturn(expenses);
        List<ExpenseDto> expenseDtos = expenseService.getAllExpenses("testuser");
        assertEquals(1, expenseDtos.size());
        assertEquals(100.0, expenseDtos.get(0).getAmount());
        assertEquals(LocalDate.parse("2023-07-10"), expenseDtos.get(0).getDate());
    }

    @Test
    void addExpense() {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(100.0);
        expenseDto.setDate(LocalDate.parse("2023-07-10"));
        expenseDto.setCategory("Food");
        expenseDto.setTitle("Test Expense");
        expenseDto.setUserName("testuser");
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setId(1L);
        Expense expense = modelMapper.map(expenseDto, Expense.class);
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(expenseRepository.save(expense)).thenReturn(expense);
        ExpenseDto addedExpenseDto = expenseService.addExpense(expense);
        assertEquals(100.0, addedExpenseDto.getAmount());
        assertEquals(LocalDate.parse("2023-07-10"), addedExpenseDto.getDate());
        assertEquals("Food", addedExpenseDto.getCategory());
        assertEquals("Test Expense", addedExpenseDto.getTitle());
        assertEquals("testuser", addedExpenseDto.getUserName());
    }

    @Test
    void getExpensesForUser() {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(100.0);
        expenseDto.setDate(LocalDate.parse("2023-07-10"));
        expenseDto.setCategory("Food");
        expenseDto.setTitle("Test Expense");
        expenseDto.setUserName("testuser");
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setId(1L);
        Expense expense = modelMapper.map(expenseDto, Expense.class);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(expense);
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(expenseRepository.findExpenseByUser_Username("testuser")).thenReturn(expenses);
        List<ExpenseDto> expenseDtos = expenseService.getExpensesForUser("testuser");
        assertEquals(1, expenseDtos.size());
        assertEquals(100.0, expenseDtos.get(0).getAmount());
        assertEquals(LocalDate.parse("2023-07-10"), expenseDtos.get(0).getDate());
        assertEquals("Food", expenseDtos.get(0).getCategory());
        assertEquals("Test Expense", expenseDtos.get(0).getTitle());
    }

    @Test
    void getExpense() {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(100.0);
        expenseDto.setDate(LocalDate.parse("2023-07-10"));
        expenseDto.setCategory("Food");
        expenseDto.setTitle("Test Expense");
        expenseDto.setUserName("testuser");
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setId(1L);
        Expense expense = modelMapper.map(expenseDto, Expense.class);
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(expenseRepository.findById(1L)).thenReturn(java.util.Optional.of(expense));
        ExpenseDto expenseDto1 = expenseService.getExpense(1L);
        assertEquals(100.0, expenseDto1.getAmount());
        assertEquals(LocalDate.parse("2023-07-10"), expenseDto1.getDate());
        assertEquals("Food", expenseDto1.getCategory());
        assertEquals("Test Expense", expenseDto1.getTitle());
        assertEquals("testuser", expenseDto1.getUserName());
    }

    @Test
    void updateExpense() {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setAmount(100.0);
        expenseDto.setDate(LocalDate.parse("2023-07-10"));
        expenseDto.setCategory("Food");
        expenseDto.setTitle("Test Expense");
        expenseDto.setUserName("testuser");
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setId(1L);
        Expense expense = modelMapper.map(expenseDto, Expense.class);
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(expenseRepository.findById(1L)).thenReturn(java.util.Optional.of(expense));
        when(expenseRepository.save(expense)).thenReturn(expense);
        ExpenseDto expenseDto1 = expenseService.updateExpense(1L, expense);
        assertEquals(100.0, expenseDto1.getAmount());
        assertEquals(LocalDate.parse("2023-07-10"), expenseDto1.getDate());
        assertEquals("Food", expenseDto1.getCategory());
        assertEquals("Test Expense", expenseDto1.getTitle());
        assertEquals("testuser", expenseDto1.getUserName());
    }

    @Test
    void deleteExpense() {
        expenseService.deleteExpense(1L);
        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExistInGetAllExpenses() {
        String nonExistingUsername = "non_existing_user";
        when(userRepository.findByUsername(nonExistingUsername)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> expenseService.getAllExpenses(nonExistingUsername));
    }

    @Test
    void getAllExpenses_shouldReturnEmptyList_whenNoExpensesFoundForUser() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setId(1L);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(expenseRepository.findExpenseByUser_Username(username)).thenReturn(Collections.emptyList());
        List<ExpenseDto> expenseDtos = expenseService.getAllExpenses(username);
        assertTrue(expenseDtos.isEmpty());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserObjectIsNullInAddExpense() {
        Expense expense = new Expense();
        expense.setTitle("Test Expense");
        expense.setAmount(100.0);
        expense.setCategory("Food");
        expense.setDate(LocalDate.parse("2023-07-10"));
        expense.setUser(null);
        assertThrows(IllegalArgumentException.class, () -> expenseService.addExpense(expense));
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExistInAddExpense() {
        Expense expense = new Expense();
        expense.setUser(new User());
        expense.getUser().setUsername("non_existing_user");
        when(userRepository.findByUsername("non_existing_user")).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> expenseService.addExpense(expense));
    }

    @Test
    void shouldNotThrowExceptionWhenDeletingExistingExpense() {
        Expense expense = new Expense();
        expense.setId(1L);
        when(expenseRepository.findById(1L)).thenReturn(java.util.Optional.of(expense));
        assertDoesNotThrow(() -> expenseService.deleteExpense(1L));
        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTryingToDeleteANonExistingExpenseInDeleteExpense() {
        long nonExistingExpenseId = 99999;
        doThrow(EmptyResultDataAccessException.class)
                .when(expenseRepository).deleteById(nonExistingExpenseId);
        assertThrows(EmptyResultDataAccessException.class, () -> expenseService.deleteExpense(nonExistingExpenseId));
    }

    @Test
    void updateExistingExpense() {
       ExpenseDto expenseDto = new ExpenseDto();
       expenseDto.setAmount(100.0);
       expenseDto.setDate(LocalDate.parse("2023-07-10"));
       expenseDto.setCategory("Food");
       expenseDto.setTitle("Test Expense");
       expenseDto.setUserName("testuser");
       User user = new User();
       user.setUsername("testuser");
       user.setPassword("password");
       user.setId(1L);
       Expense expense = modelMapper.map(expenseDto, Expense.class);
       when(userRepository.findByUsername("testuser")).thenReturn(user);
       when(expenseRepository.findById(1L)).thenReturn(java.util.Optional.of(expense));
       when(expenseRepository.save(expense)).thenReturn(expense);
       ExpenseDto expenseDto1 = expenseService.updateExpense(1L, expense);
       assertEquals(100.0, expenseDto1.getAmount());
       assertEquals(LocalDate.parse("2023-07-10"), expenseDto1.getDate());
       assertEquals("Food", expenseDto1.getCategory());
       assertEquals("Test Expense", expenseDto1.getTitle());
       assertEquals("testuser", expenseDto1.getUserName());
    }
}