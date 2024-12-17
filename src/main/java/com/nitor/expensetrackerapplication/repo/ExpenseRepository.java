package com.nitor.expensetrackerapplication.repo;

import com.nitor.expensetrackerapplication.model.Expense;
import com.nitor.expensetrackerapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);

    boolean existsByIdAndUserId(Long id, Long currentUserId);

    List<Expense> findExpenseByUser_Username(String userUsername);
}
