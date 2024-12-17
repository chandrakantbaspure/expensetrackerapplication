package com.nitor.expensetrackerapplication.controller;

import com.nitor.expensetrackerapplication.dto.ExpenseDto;
import com.nitor.expensetrackerapplication.model.Expense;
import com.nitor.expensetrackerapplication.service.ExpenseService;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin("*")
public class ExpenseController {
    private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);
    private final ExpenseService expenseService;

    private final ModelMapper mapper;

    @Autowired
    public ExpenseController(ExpenseService expenseService, ModelMapper mapper) {
        this.expenseService = expenseService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@RequestBody ExpenseDto expenseDto) {
        log.info("Adding expense: {}", expenseDto);
        Expense expense = mapper.map(expenseDto, Expense.class);
        return ResponseEntity.ok(mapper.map(expenseService.addExpense(expense), ExpenseDto.class));
    }

    @GetMapping("/all/{username}")
    public List<ExpenseDto> getAllExpenses(@PathVariable String username) {
        log.info("Fetching all expenses for user: {}", username);
        return expenseService.getAllExpenses(username);
    }

    @GetMapping("/{expenseId}")
    public ExpenseDto getExpensesById(@PathVariable Long expenseId) {
        log.info("Fetching expense with id: {}", expenseId);
        return expenseService.getExpense(expenseId);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {
        log.info("Deleting expense with id: {}", id);
        expenseService.deleteExpense(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        log.info("Updating expense with id: {}", id);
        return ResponseEntity.ok(expenseService.updateExpense(id, expense));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<ExpenseDto>> getExpensesForUser(@PathVariable String username) {
        log.info("Fetching expenses for user: {}", username);
        try {
            List<ExpenseDto> expenses = expenseService.getExpensesForUser(username);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            log.error("Error fetching expenses for user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
