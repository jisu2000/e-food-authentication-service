package com.subhadeep.e_food_authentication_service.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.subhadeep.e_food_authentication_service.service.FileUploadserService;
import com.subhadeep.e_food_authentication_service.utils.CloudinaryFileUploadUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryFileUploaderServiceImpl implements FileUploadserService {

    private final CloudinaryFileUploadUtil cloudinaryFileUploadUtil;

    @Override
    public String uploadFile(MultipartFile multipartFil, String folder) {
        Map<?, ?> response = cloudinaryFileUploadUtil.uploadImage(multipartFil, folder);
        String url = (String) response.get("url");
        return url == null ? "" : url;

    }

}
