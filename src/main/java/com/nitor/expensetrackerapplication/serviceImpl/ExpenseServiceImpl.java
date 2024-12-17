package com.nitor.expensetrackerapplication.serviceImpl;

import com.nitor.expensetrackerapplication.dto.ExpenseDto;
import com.nitor.expensetrackerapplication.exception.UserNotFoundException;
import com.nitor.expensetrackerapplication.model.Expense;
import com.nitor.expensetrackerapplication.model.User;
import com.nitor.expensetrackerapplication.repo.ExpenseRepository;
import com.nitor.expensetrackerapplication.repo.UserRepository;
import com.nitor.expensetrackerapplication.service.ExpenseService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private ModelMapper mapper;

    private UserRepository userRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, ModelMapper mapper, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }


    @Override
    public List<ExpenseDto> getAllExpenses(String username) {
        log.info("Getting all expenses for user: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found: {}", username);
            throw new UserNotFoundException("User not found");
        }
        List<Expense> expenses = expenseRepository.findExpenseByUser_Username(username);
        return expenses.stream()
                .map(expense -> mapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toList());

    }

    @Transactional
    @Override
    public ExpenseDto addExpense(Expense expense) {
        log.info("Adding expense for user: {}", expense.getUser());

        if (expense.getUser() == null) {
            throw new IllegalArgumentException("User object is null");
        }

        String username = expense.getUser().getUsername();
        log.info("User found: {}", username);

        User existingUser = userRepository.findByUsername(username);
        log.info("Existing user found: {}", existingUser);

        if (existingUser == null) {
            throw new UserNotFoundException("User not found");
        }

        expense.setUser(existingUser);

        log.info("Expense saved with user: {}", existingUser);

        Random random = new Random();
        expense.setId(random.nextLong(10000));

        Expense savedExpense = expenseRepository.save(expense);
        return mapper.map(savedExpense, ExpenseDto.class);
    }



    public List<ExpenseDto> getExpensesForUser(String username) {
        log.info("Getting expenses for user: {}", username);
        List<Expense> expenses = expenseRepository.findExpenseByUser_Username(username);
        return expenses.stream()
                .map(expense -> mapper.map(expense, ExpenseDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public ExpenseDto getExpense(Long id) {
        log.info("Getting expense with id: {}", id);
        return mapper.map(expenseRepository.findById(id).orElse(null), ExpenseDto.class);
    }

    @Override
    public ExpenseDto updateExpense(Long id, Expense expense) {
        log.info("Updating expense with id: {}", id);
        Expense existingExpense = expenseRepository.findById(id).orElse(null);
        if (existingExpense != null) {
            existingExpense.setTitle(expense.getTitle());
            existingExpense.setAmount(expense.getAmount());
            existingExpense.setCategory(expense.getCategory());
            existingExpense.setDate(expense.getDate());
            return mapper.map(expenseRepository.save(existingExpense), ExpenseDto.class);
        }
        return null;
    }

    @Override
    public void deleteExpense(Long id) {
        log.info("Deleting expense with id: {}", id);
        expenseRepository.deleteById(id);

    }
}
