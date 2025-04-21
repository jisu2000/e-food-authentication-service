package com.subhadeep.e_food_authentication_service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.subhadeep.e_food_authentication_service.constant.RegexConstant;
import com.subhadeep.e_food_authentication_service.exceptions.ResourceNotFoundException;
import com.subhadeep.e_food_authentication_service.model.UserEO;
import com.subhadeep.e_food_authentication_service.repo.UserRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        boolean isEmail = username.matches(RegexConstant.EMAIL_REGEX);
        UserEO fetchedUser = isEmail ? userRepo.findByEmailIgnoreCase(username)
                : userRepo.findByMobileNo(username);

        if (fetchedUser == null) {
            throw new ResourceNotFoundException("User", isEmail ? "Email" : "MobileNo", username);
        }

        return new AuthUser(fetchedUser);

    }

}
