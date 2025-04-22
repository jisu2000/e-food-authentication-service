package com.subhadeep.e_food_authentication_service.service;

import java.util.Map;

public interface RefreshTokenService {
    String generateRefreshToken(Integer userId);
    Map<?,?> refreshToken(String token);
}
