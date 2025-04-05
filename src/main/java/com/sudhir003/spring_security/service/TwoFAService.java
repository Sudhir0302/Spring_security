package com.sudhir003.spring_security.service;

import com.sudhir003.spring_security.model.User;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

@Service
public class TwoFAService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    public String generateSecret(User user) {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        user.setSecretKey(key.getKey()); // Save to DB later
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("SpringApp", user.getUsername(), key);
    }

    public boolean verifyCode(String secret, int code) {
        return gAuth.authorize(secret, code);
    }
}
