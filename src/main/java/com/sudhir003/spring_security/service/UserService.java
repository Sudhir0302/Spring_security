package com.sudhir003.spring_security.service;

import com.sudhir003.spring_security.model.Role;
import com.sudhir003.spring_security.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);
    User getUser(String username);
    List<User> getUsers();
}
