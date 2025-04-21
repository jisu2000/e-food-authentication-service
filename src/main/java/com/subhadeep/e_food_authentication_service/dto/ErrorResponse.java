package com.subhadeep.e_food_authentication_service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ErrorResponse {
    
    private String error;
    private List<String> subErrors;
    @JsonIgnore
    private Integer status;  
}
