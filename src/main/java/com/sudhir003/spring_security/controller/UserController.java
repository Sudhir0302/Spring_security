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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
public class UserController{

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TwoFAService twoFAService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> adduser(@RequestBody  User user)
    {
        User user1=userService.finduser(user.getUsername());
        System.out.println(user1);
        if(user1!=null){
            return new ResponseEntity<>("username already exits",HttpStatus.BAD_REQUEST);
        }
        String qrurl=twoFAService.generateSecret(user);
        userService.saveUser(user);
        return ResponseEntity.ok(qrurl);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String username, @RequestParam int code) {
        User user = userService.finduser(username);

        if (user == null) {
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        }

        boolean isCodeValid = twoFAService.verifyCode(user.getSecretKey(), code); //checking if OTP and secretkey is same or not

        if (isCodeValid) {
            String jwt = jwtService.generateToken(username);
            return ResponseEntity.ok(jwt);
        } else {
            return new ResponseEntity<>("Invalid OTP",HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user)
    {
        //spring security has a in-build userpasswordauthenticationtoken ,which checks username and password and generate token.
        //AuthenticationManager uses a list of AuthenticationProviders to validate credentials. One of the provider is dao.
        Authentication authentication=authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if(authentication.isAuthenticated()){
            return ResponseEntity.ok("Password validated.Now enter OTP");
        }
        return new ResponseEntity<>("failed",HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getuser")
    public ResponseEntity<?> getuser(@RequestParam String username)
    {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        if(!auth.getName().equals(username)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user=userService.finduser(username);
        if(user==null){
            return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
