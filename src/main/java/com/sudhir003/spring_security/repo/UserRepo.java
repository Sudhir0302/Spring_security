package com.sudhir003.spring_security.repo;

import com.sudhir003.spring_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
