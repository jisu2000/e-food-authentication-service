package com.subhadeep.e_food_authentication_service.service;

public interface RefreshTokenService {
    String generateRefreshToken(Integer userId);
}
