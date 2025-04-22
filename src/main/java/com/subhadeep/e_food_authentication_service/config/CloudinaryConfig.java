package com.subhadeep.e_food_authentication_service.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
    

    @Value("${cloudinary.cloud.name}")
    private String cloudName;
    @Value("${cloudinary.access.key}")
    private String apiKeyName;
    @Value("${cloudinary.access.secret}")
    private String apiKeySec;


  @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("cloud_name", cloudName);
        configMap.put("api_key", apiKeyName);
        configMap.put("api_secret", apiKeySec);
        configMap.put("secure", true);

        return new Cloudinary(configMap);
    }

}
