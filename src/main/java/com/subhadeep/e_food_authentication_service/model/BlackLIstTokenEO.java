package com.subhadeep.e_food_authentication_service.model;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BlackLIstTokenEO extends BaseEO{
    private String token;
    private LocalDateTime timeToBeStored;
}
