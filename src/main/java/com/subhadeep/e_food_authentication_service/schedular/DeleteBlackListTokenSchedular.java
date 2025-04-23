package com.subhadeep.e_food_authentication_service.schedular;

import com.subhadeep.e_food_authentication_service.model.BlackLIstTokenEO;
import com.subhadeep.e_food_authentication_service.repo.BlackListedTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteBlackListTokenSchedular {

    private final BlackListedTokenRepo blackListedTokenRepo;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredBlackListedToken(){
        List<BlackLIstTokenEO> expiredBlackListedTokens
                = blackListedTokenRepo.findByTimeToBeStoredBefore(LocalDateTime.now());

        if(!expiredBlackListedTokens.isEmpty()){
            blackListedTokenRepo.deleteAll(expiredBlackListedTokens);
        }
    }
}
