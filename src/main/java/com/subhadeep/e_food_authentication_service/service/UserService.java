package com.subhadeep.e_food_authentication_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.subhadeep.e_food_authentication_service.dto.PaginatedResponse;
import com.subhadeep.e_food_authentication_service.dto.UserDTO;

import jakarta.validation.Valid;


public interface UserService {
    Map<?,?> registerAsCustomer( @Valid UserDTO data,MultipartFile multipartFile);
    UserDTO getSingleUser(Integer userId);
    Map<?,?> deleteUser(Integer userId);
    PaginatedResponse getAllUsers(Integer pageNo, Integer pageSize,String sortBy, String sortDir);
    Map<?,?> loginUser(Map<String,String> loginRequest);
    UserDTO getCurrentUser();
    Map<?,?> userDeletedBySelf();
}
