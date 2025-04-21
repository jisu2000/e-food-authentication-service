package com.subhadeep.e_food_authentication_service.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.subhadeep.e_food_authentication_service.model.UserEO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    public boolean doAuthenticate(String userName, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName,
                password);

        boolean authenticated = false;

        try {
            authenticationManager.authenticate(authentication);

            authenticated = !authenticated;

        } catch (Exception e) {
        }

        return authenticated;
    }

    public UserEO getCurrentUserObject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AuthUser) {

                UserEO user = ((AuthUser) principal).getUser();
                return user;
            }
        }
        return null;
    }

}
