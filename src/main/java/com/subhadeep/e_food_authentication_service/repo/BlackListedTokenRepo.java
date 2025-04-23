package com.subhadeep.e_food_authentication_service.repo;

import com.subhadeep.e_food_authentication_service.model.BlackLIstTokenEO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListedTokenRepo extends JpaRepository<BlackLIstTokenEO,Integer> {
    BlackLIstTokenEO findByToken(String token);
}
