package com.subhadeep.e_food_authentication_service.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.subhadeep.e_food_authentication_service.constant.PageConstant;
import com.subhadeep.e_food_authentication_service.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SecureController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllUser(
            @RequestParam(value = "pageNo", required = false, defaultValue = PageConstant.DEFAULT_PAGE_NO) Integer pageNo,

            @RequestParam(value = "pageSize", required = false, defaultValue = PageConstant.DEFAULT_PAGE_SIZE) Integer pageSize,

            @RequestParam(value = "sortBy", required = false, defaultValue = PageConstant.DEFAULT_SORT_BY) String sortBy,

            @RequestParam(value = "sortDir", required = false, defaultValue = PageConstant.DEFAULT_SORT_DIR) String sortDir

    ) {
        return new ResponseEntity<>(userService
                .getAllUsers(pageNo, pageSize, sortBy, sortDir),
                HttpStatus.OK);
    }

    @GetMapping("/current/user")
    public ResponseEntity<?> getAuthenticatedUser(){
        return new ResponseEntity<>(userService.getCurrentUser(),HttpStatus.OK);
    }

    @DeleteMapping("/delete/self")
    public ResponseEntity<?> deleteUser(){
        return new ResponseEntity<>(userService.userDeletedBySelf(),HttpStatus.OK);
    }

}
