package com.subhadeep.e_food_authentication_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.subhadeep.e_food_authentication_service.constant.TokenExpiryConstant;
import com.subhadeep.e_food_authentication_service.model.RefreshTokenEO;
import com.subhadeep.e_food_authentication_service.repo.RefreshTokenRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepo refreshTokenRepo;

    @Override
    public String generateRefreshToken(Integer userId) {
        RefreshTokenEO refreshTokenEO = new RefreshTokenEO();
        refreshTokenEO.setToken("RT"+UUID.randomUUID().toString());
        refreshTokenEO.setExpireTime(LocalDateTime.now().plusSeconds(TokenExpiryConstant.REFRESH_TOKEN_EXPIRY_IN_SECOND));
        refreshTokenEO.setUserId(userId);

        RefreshTokenEO saved = refreshTokenRepo.save(refreshTokenEO);

        return saved.getToken();

    }
    
}
