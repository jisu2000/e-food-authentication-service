package com.subhadeep.e_food_authentication_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadserService {
    String uploadFile(MultipartFile multipartFile, String folder);
}
