package com.subhadeep.e_food_authentication_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.subhadeep.e_food_authentication_service.model.UserEO;

public interface UserRepo extends JpaRepository<UserEO,Integer>{
    UserEO findByEmailIgnoreCase(String email);
    UserEO findByMobileNo(String mobileNo);
}
