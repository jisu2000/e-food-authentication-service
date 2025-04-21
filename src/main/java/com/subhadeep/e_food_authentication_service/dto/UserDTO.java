package com.subhadeep.e_food_authentication_service.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.subhadeep.e_food_authentication_service.constant.RegexConstant;
import com.subhadeep.e_food_authentication_service.constant.ValidationConstant;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private Integer id;
    private String createdAt;
    private String updatedAt;
    @NotNull(message = "Full name can not be NULL")
    @NotEmpty(message = "Full name can not Empty")
    private String fullName;
    @NotBlank(message = "Email can not be Empty")
    @NotNull(message = "Email can not be NULL")
    @Pattern(regexp = RegexConstant.EMAIL_REGEX, message = "Must be a valid Email")
    private String email;

    @NotBlank(message = "Mobile no can not be Empty")
    @NotNull(message = "Mobile no not be NULL")
    private String mobileNo;
    private String profilePhoto;

    @NotBlank(message = "Password no can not be Empty")
    @NotNull(message = "Password no not be NULL")
    @Size(
        min = ValidationConstant.MIN_PASSWORD_LENGTH,
        max = ValidationConstant.MAX_PASSWORD_LENGTH, 
        message = "Password length should be minimum "+ValidationConstant.MIN_PASSWORD_LENGTH+" and maximum "+ValidationConstant.MAX_PASSWORD_LENGTH
        )
    private String password;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    private Set<RoleDTO> roles;

}
