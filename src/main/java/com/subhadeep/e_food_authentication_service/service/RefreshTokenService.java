package com.subhadeep.e_food_authentication_service.service;

import com.subhadeep.e_food_authentication_service.model.RefreshTokenEO;

import java.util.List;
import java.util.Map;

public interface RefreshTokenService {
    String generateRefreshToken(Integer userId);
    Map<?,?> refreshToken(String token);
    List<RefreshTokenEO> getAllUserTokens(Integer userId);
    void deleteAllUserRefreshToken(Integer userId);
}
