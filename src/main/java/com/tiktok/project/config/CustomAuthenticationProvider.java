package com.tiktok.project.config;

import com.tiktok.project.service.ImpService.UserImpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserImpService userImpService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String identifier = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails = null;

        if (isValidEmail(identifier)) {
            userDetails = userImpService.loadUserByEmail(identifier);
        } else if (isValidPhoneNumber(identifier)) {
            userDetails = userImpService.loadUserByPhoneNumber(identifier);
        } else {
            userDetails = userImpService.loadUserByUsername(identifier);
        }

        if (userDetails != null) {
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }


    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d+"); // Giả sử số điện thoại chỉ chứa số
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}