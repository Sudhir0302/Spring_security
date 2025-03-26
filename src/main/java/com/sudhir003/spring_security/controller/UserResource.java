package com.sudhir003.spring_security.controller;

import com.sudhir003.spring_security.model.Role;
import com.sudhir003.spring_security.model.User;
import com.sudhir003.spring_security.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserResource {
    @Autowired
    private UserService service;

    @GetMapping("/users")
    public ResponseEntity<List<User>>getUsers(){
        return ResponseEntity.ok().body(service.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User>saveUser(@RequestBody User user)
    {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/user/save")
                .build()
                .toUri();
        return ResponseEntity.created(uri).body(service.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveUser(@RequestBody Role role)
    {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/role/save")
                .build()
                .toUri();
        return ResponseEntity.created(uri).body(service.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?>addRoletToUser(@RequestBody RoleToUserForm role)
    {
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/role/save")
                .build()
                .toUri();
        service.addRoleToUser(role.getUsername(),role.getRoleName());
        return ResponseEntity.ok().build();  //returns response without body
    }

}

//just like DTO
@Data
class RoleToUserForm{
    private String username;
    private String roleName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
