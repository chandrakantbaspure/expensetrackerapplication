package com.nitor.expensetrackerapplication.repo;

import com.nitor.expensetrackerapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
