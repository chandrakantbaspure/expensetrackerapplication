package com.nitor.expensetrackerapplication.service;

import com.nitor.expensetrackerapplication.dto.ExpenseDto;
import com.nitor.expensetrackerapplication.model.Expense;

import java.util.List;

public interface ExpenseService {
    List<ExpenseDto> getAllExpenses(String username);
    ExpenseDto addExpense(Expense expense);
    ExpenseDto getExpense(Long id);
    ExpenseDto updateExpense(Long id, Expense expense);
    void deleteExpense(Long id);

    List<ExpenseDto> getExpensesForUser(String username);
}
