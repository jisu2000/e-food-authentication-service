package com.subhadeep.e_food_authentication_service.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserEO extends BaseEO{
    private String fullName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String mobileNo;
    private String profilePhoto;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")

    )
    private Set<RoleEO> roles;
    
}
