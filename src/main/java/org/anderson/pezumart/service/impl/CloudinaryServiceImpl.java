package org.anderson.pezumart.service.impl;

import com.cloudinary.Cloudinary;
import org.anderson.pezumart.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map<String, String> uploadImage(MultipartFile file, String folderName) {
        try {
            HashMap<Object, Object> options = new HashMap<>();
            String publicIdOption = file.getName() + "_" + UUID.randomUUID();

            options.put("folder", folderName);
            options.put("public_id", publicIdOption);

            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            String url = cloudinary.url().secure(true).generate(publicId);
            return Map.of(
                    "publicId", publicId,
                    "url", url
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteImage(String publicId) {
        Map<String, Object> options = Map.of(
                "resource_type", "image",
                "invalidate", true
        );

        try {
            cloudinary.uploader().destroy(publicId, options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
