package com.sudhir003.spring_security.controller;

import com.sudhir003.spring_security.model.User;
import com.sudhir003.spring_security.service.EmailService;
import com.sudhir003.spring_security.service.JwtService;
import com.sudhir003.spring_security.service.TwoFAService;
import com.sudhir003.spring_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

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

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
//    private static ConcurrentHashMap<String,Integer>store_otp=new ConcurrentHashMap<>();

    @PostMapping("/register")
    public ResponseEntity<?> adduser(@RequestBody  User user)
    {
        User user1=userService.finduser(user.getUsername());
        System.out.println(user1);
        if(user1!=null){
            return new ResponseEntity<>("username already exits",HttpStatus.BAD_REQUEST);
        }
//        String qrurl=twoFAService.generateSecret(user);
        user.setIs2FA(false);
        userService.saveUser(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
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

//        System.out.print(userService.getClass());
        User user1=userService.finduser(user.getUsername());

        if(authentication.isAuthenticated()&&user1.getIs2FA()){
            return new ResponseEntity<>("Password validated.Now enter TOTP ",HttpStatus.OK);
        }else if(authentication.isAuthenticated()){
            return new ResponseEntity<>(jwtService.generateToken(user1.getUsername()),HttpStatus.OK);
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

//    testing endpoint
    @GetMapping("/sendmail")
    public ResponseEntity<?> sendMail(@RequestParam String mailid){
        try {
            emailService.sendEmail(mailid, "test", "this mail service");
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("mail not send!!",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("mail send!!",HttpStatus.OK);
    }

    @GetMapping("/enable2FA")
    public ResponseEntity<?> enable2FA(@RequestParam String username)
    {
        User user=userService.finduser(username);
        user.setIs2FA(true);
        String QRurl=twoFAService.generateSecret(user);
        userService.updateUser(user);
        return new ResponseEntity<>(QRurl,HttpStatus.CREATED);
    }

    @GetMapping("/disable2FA")
    public ResponseEntity<?> disable2FA(@RequestParam String username)
    {
        User user=userService.finduser(username);
        user.setIs2FA(false);
        user.setSecretKey(null);
        userService.updateUser(user);
        return new ResponseEntity<>("disabled 2fa",HttpStatus.OK);
    }


    @GetMapping("/recoverAccount")
    public ResponseEntity<?>recover2FA(@RequestParam String username){
        User user=userService.finduser(username);
        if(user!=null){
            SecureRandom random = new SecureRandom();
            int otp=100000 + random.nextInt(900000);
            try {
//                store_otp.put(username,otp);
                redisTemplate.opsForValue().set(username, String.valueOf(otp), Duration.ofMinutes(5));
                emailService.sendEmail(username, "OTP for account/2fa recovery", String.valueOf(otp));
                return new ResponseEntity<>("otp send via email",HttpStatus.OK);
            }catch (Exception e){
//                System.out.println(e);
                return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/verifyotp")
    public ResponseEntity<?>verifyOTP(@RequestParam String username,@RequestParam int otp)
    {
        Object obj = redisTemplate.opsForValue().get(username);

        if (obj == null) {
            return new ResponseEntity<>("OTP expired or not found", HttpStatus.BAD_REQUEST);
        }
        int storedOtp = Integer.parseInt(obj.toString());
        if (storedOtp == otp) {
            User user = userService.finduser(username);
            user.setSecretKey(null);
            String QRurl = twoFAService.generateSecret(user);
            userService.updateUser(user);
            return new ResponseEntity<>(QRurl, HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
    }

}
