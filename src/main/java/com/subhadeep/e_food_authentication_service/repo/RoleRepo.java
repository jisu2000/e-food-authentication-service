package com.subhadeep.e_food_authentication_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.subhadeep.e_food_authentication_service.model.RoleEO;

public interface RoleRepo extends JpaRepository<RoleEO,Integer>{
    
}
