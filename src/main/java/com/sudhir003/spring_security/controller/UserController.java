package com.sudhir003.spring_security.controller;

import com.sudhir003.spring_security.model.User;
import com.sudhir003.spring_security.service.JwtService;
import com.sudhir003.spring_security.service.TwoFAService;
import com.sudhir003.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController{

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TwoFAService twoFAService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("register")
    public ResponseEntity<?> adduser(@RequestBody  User user)
    {
        String qrurl=twoFAService.generateSecret(user);
        userService.saveUser(user);
        return ResponseEntity.ok(qrurl);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String username, @RequestParam int code) {
        User user = userService.finduser(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        boolean isCodeValid = twoFAService.verifyCode(user.getSecretKey(), code);

        if (isCodeValid) {
            String jwt = jwtService.generateToken(username);
            return ResponseEntity.ok(jwt);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }
    }


    @PostMapping("login")
    public String login(@RequestBody User user)
    {
        //spring security has a in-build userpasswordauthenticationtoken ,which checks username and password and generate token.
        Authentication authentication=authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if(authentication.isAuthenticated()){
            return  jwtService.generateToken(user.getUsername());
        }
        return "failed";
    }

    @GetMapping("getuser")
    public ResponseEntity<?> getuser(@RequestParam String username)
    {
        User user=userService.finduser(username);
        if(user==null){
            return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
