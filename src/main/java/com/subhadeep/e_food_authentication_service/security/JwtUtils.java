package com.subhadeep.e_food_authentication_service.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.subhadeep.e_food_authentication_service.constant.TokenExpiryConstant;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public void blacklistToken(String token) {

        if (isTokenBlacklisted(token)) {
            throw new IllegalArgumentException("Token is blacklisted");
        }

    }

    public boolean isTokenBlacklisted(String token) {
        return false;
    }

    public String getUsernameFromToken(String token) {
        if (isTokenBlacklisted(token)) {
            throw new IllegalArgumentException("Token is blacklisted");
        }
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Retrieve claims from token using a claims resolver
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        if (isTokenBlacklisted(token)) {
            throw new IllegalArgumentException("Token is blacklisted");
        }
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        if (isTokenBlacklisted(token)) {
            throw new IllegalArgumentException("Token is blacklisted");
        }
        return Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        if (isTokenBlacklisted(token)) {
            throw new IllegalArgumentException("Token is blacklisted");
        }
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {

        AuthUser authUser = (AuthUser) userDetails;

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        claims.put("id", authUser.getUserId());

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TokenExpiryConstant.JWT_EXPIRY_IN_SECOND * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        if (isTokenBlacklisted(token)) {
            return false;
        }
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public List<String> getRolesFromToken(String token) {
        if (isTokenBlacklisted(token)) {
            throw new IllegalArgumentException("Token is blacklisted");
        }
        Claims claims = getAllClaimsFromToken(token);
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");
        return roles != null ? roles : Collections.emptyList();
    }
}
