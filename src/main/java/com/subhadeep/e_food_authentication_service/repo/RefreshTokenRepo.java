package com.subhadeep.e_food_authentication_service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.subhadeep.e_food_authentication_service.model.RefreshTokenEO;


public interface RefreshTokenRepo extends JpaRepository<RefreshTokenEO,Integer>{
    RefreshTokenEO findByToken(String token);
    List<RefreshTokenEO> findByUserId(Integer userId);
}
