package com.subhadeep.e_food_authentication_service.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class RefreshTokenEO extends BaseEO{
    private String token;
    private LocalDateTime expireTime;
    private Integer userId;
}
