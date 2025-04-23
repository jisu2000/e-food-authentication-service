package com.subhadeep.e_food_authentication_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.subhadeep.e_food_authentication_service.dto.UserDTO;
import com.subhadeep.e_food_authentication_service.service.RefreshTokenService;
import com.subhadeep.e_food_authentication_service.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final RefreshTokenService refreshTokenService;

    @PostMapping(path = "/register/customer", consumes = { "multipart/form-data" })
    public ResponseEntity<?> registerUser(
            @RequestPart(value = "data") String data,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return new ResponseEntity<>(
                userService.registerAsCustomer(getDtofromString(data), file),
                HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody Map<String, String> body) {
        return new ResponseEntity<>(userService.loginUser(body), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestHeader("RefreshToken") String token) {
        return new ResponseEntity<>(refreshTokenService.refreshToken(token), HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logoutUser(
            @RequestHeader("Authorization") String authHeader
    ){
        return new ResponseEntity<>(userService.doLogOut(authHeader),HttpStatus.OK);
    }


    private UserDTO getDtofromString(String data) {

        UserDTO userDTO = new UserDTO();

        Map<String, Object> dataMap = new HashMap<>();
        try {
            dataMap = objectMapper.readValue(data,
                    new TypeReference<Map<String, Object>>() {
                    });

            userDTO.setEmail(dataMap.getOrDefault("email", "").toString());
            userDTO.setFullName(dataMap.getOrDefault("fullName", "").toString());
            userDTO.setMobileNo(dataMap.getOrDefault("mobileNo", "").toString());
            userDTO.setPassword(dataMap.getOrDefault("password", "").toString());

        } catch (Exception e) {

        }

        return userDTO;
    }

}
