package com.subhadeep.e_food_authentication_service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.subhadeep.e_food_authentication_service.model.UserEO;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class AuthUser implements UserDetails {

    private UserEO userEO;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEO.getRoles()
                .stream()
                .map(e -> new SimpleGrantedAuthority("ROLE_" + e.getRoleName()))
                .toList();
    }

    @Override
    public String getPassword() {
        return userEO.getPassword();
    }

    @Override
    public String getUsername() {
        return userEO.getEmail();
    }

    public Integer getUserId(){
        return userEO.getId();
    }

    public UserEO getUser(){
        return userEO;
    }

}
