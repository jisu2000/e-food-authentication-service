package com.subhadeep.e_food_authentication_service.utils;

import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryFileUploadUtil {
    private final Cloudinary cloudinary;

    public Map<?, ?> uploadImage(MultipartFile file, String folderName) {

        Map<?, ?> uploadedImageMap = null;
        try {
            uploadedImageMap = cloudinary.uploader().upload(file.getBytes(), Map.of("folder", folderName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uploadedImageMap;
    }
}
