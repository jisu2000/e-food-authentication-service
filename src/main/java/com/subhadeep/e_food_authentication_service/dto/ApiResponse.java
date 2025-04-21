package com.subhadeep.e_food_authentication_service.dto;


import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ApiResponse <T>{
    private T data;
    private Integer status;
    private boolean success;
    private String timeStamp;
    private ErrorResponse error;


    public ApiResponse(){
        this.timeStamp = LocalDateTime.now().toString();
    }

    public ApiResponse(T data){
        this();
        this.data = data;
        this.success = true;
        status = 200;
    }

    public ApiResponse(ErrorResponse errorResponse){
        this.error = errorResponse;
        success = false;
        this.status = errorResponse.getStatus();
        
    }
}
