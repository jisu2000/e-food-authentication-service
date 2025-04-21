package com.subhadeep.e_food_authentication_service.config;

import java.util.List;

import org.springframework.stereotype.Component;

import com.subhadeep.e_food_authentication_service.constant.RoleConstants;
import com.subhadeep.e_food_authentication_service.model.RoleEO;
import com.subhadeep.e_food_authentication_service.repo.RoleRepo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleConfig {

    private final RoleRepo roleRepo;

    @PostConstruct
    public void insertRole() {

        List<RoleEO> missingRole = RoleConstants.idRoleMap.keySet()
                .stream().filter(e -> roleRepo.findById(e).isEmpty())
                .map(e -> RoleConstants.idRoleMap.get(e))
                .toList();

        roleRepo.saveAll(missingRole);

    }
}
