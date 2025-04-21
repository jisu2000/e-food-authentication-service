package com.subhadeep.e_food_authentication_service.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
public class RoleEO {
    @Id
    private Integer id;
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEO> users;
}
