package com.sudhir003.spring_security.repo;

import com.sudhir003.spring_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Integer>
{
    User findByUsername(String username);
}
