package com.subhadeep.e_food_authentication_service.config;

import com.subhadeep.e_food_authentication_service.constant.RoleConstants;
import com.subhadeep.e_food_authentication_service.model.RoleEO;
import com.subhadeep.e_food_authentication_service.model.UserEO;
import com.subhadeep.e_food_authentication_service.repo.RoleRepo;
import com.subhadeep.e_food_authentication_service.repo.UserRepo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(10)
@RequiredArgsConstructor
public class AdminConfig {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.mobileno}")
    private String adminMobileNo;
    @Value("${admin.name}")
    private String adminName;
    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    public void saveFirstAdmin(){
        boolean isFirstAdminPresent = userRepo.findByEmailIgnoreCase(adminEmail)!=null;

        if(!isFirstAdminPresent){
            UserEO admin = new UserEO();
            admin.setEmail(adminEmail);
            admin.setFullName(adminName);
            admin.setMobileNo(adminMobileNo);
            admin.setPassword(passwordEncoder.encode(adminPassword));

            Set<RoleEO> roles = new HashSet<>();
            RoleEO adminRole = roleRepo.findById(RoleConstants.ADMIN_ID).get();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepo.save(admin);

        }

    }
}
