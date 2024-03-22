package org.anderson.pezumart.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map<String, String> uploadImage(MultipartFile file, String folderName);
    void deleteImage(String publicId);
}
