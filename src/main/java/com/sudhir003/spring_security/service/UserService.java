package com.sudhir003.spring_security.service;

import com.sudhir003.spring_security.model.User;
import com.sudhir003.spring_security.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    public User saveUser(User user)
    {
        user.setPassword(encoder.encode(user.getPassword()));
        return  userRepo.save(user);
    }

    public User finduser(String username)
    {
        return userRepo.findByUsername(username);
    }

    public User updateUser(User user)
    {
        return userRepo.save(user);
    }
}
