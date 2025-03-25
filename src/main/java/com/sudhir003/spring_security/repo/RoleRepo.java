package com.sudhir003.spring_security.repo;

import com.sudhir003.spring_security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
