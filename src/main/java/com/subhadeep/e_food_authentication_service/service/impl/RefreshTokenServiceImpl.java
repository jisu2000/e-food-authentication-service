package com.subhadeep.e_food_authentication_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.subhadeep.e_food_authentication_service.constant.TokenExpiryConstant;
import com.subhadeep.e_food_authentication_service.exceptions.ResourceNotFoundException;
import com.subhadeep.e_food_authentication_service.exceptions.UnauthorizeException;
import com.subhadeep.e_food_authentication_service.model.RefreshTokenEO;
import com.subhadeep.e_food_authentication_service.model.UserEO;
import com.subhadeep.e_food_authentication_service.repo.RefreshTokenRepo;
import com.subhadeep.e_food_authentication_service.repo.UserRepo;
import com.subhadeep.e_food_authentication_service.security.AuthUser;
import com.subhadeep.e_food_authentication_service.security.JwtUtils;
import com.subhadeep.e_food_authentication_service.service.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;
    private final JwtUtils jwtUtils;
    private final UserRepo userRepo;

    @Override
    public String generateRefreshToken(Integer userId) {
        RefreshTokenEO refreshTokenEO = new RefreshTokenEO();
        refreshTokenEO.setToken("RT" + UUID.randomUUID().toString());
        refreshTokenEO
                .setExpireTime(LocalDateTime.now().plusSeconds(TokenExpiryConstant.REFRESH_TOKEN_EXPIRY_IN_SECOND));
        refreshTokenEO.setUserId(userId);

        RefreshTokenEO saved = refreshTokenRepo.save(refreshTokenEO);

        return saved.getToken();

    }

    @Override
    public Map<?, ?> refreshToken(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizeException("Missing Refresh Token");
        }

        RefreshTokenEO refreshTokenEO = refreshTokenRepo.findByToken(token);

        if (refreshTokenEO == null) {
            throw new UnauthorizeException("Invalid Refresh Token");
        }

        if (refreshTokenEO.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new UnauthorizeException("Refresh token Expired");
        }

        UserEO fetchedUserFromToken = userRepo.findById(refreshTokenEO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User data not found"));

        AuthUser user = new AuthUser(fetchedUserFromToken);

        String jwtToken = jwtUtils.generateToken(user);

        Cookie cookieJwt = new Cookie("jwtToken", jwtToken);
        cookieJwt.setSecure(true);
        cookieJwt.setHttpOnly(true);

        Cookie cookieRe = new Cookie("refreshToken", token);
        cookieRe.setHttpOnly(true);
        cookieRe.setSecure(true);

        HttpServletResponse response = getCurrentResponse();
        response.addCookie(cookieJwt);
        response.addCookie(cookieRe);

        return Map.of("msg", "Access token Refreshed");

    }

    @Override
    public List<RefreshTokenEO> getAllUserTokens(Integer userId) {
       return refreshTokenRepo.findByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteAllUserRefreshToken(Integer userId) {
        refreshTokenRepo.deleteByUserId(userId);
    }

    public HttpServletResponse getCurrentResponse() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getResponse() : null;
    }

}
