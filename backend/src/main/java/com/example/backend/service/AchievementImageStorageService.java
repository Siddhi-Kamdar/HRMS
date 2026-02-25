package com.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class AchievementImageStorageService {

    @Value("${file.upload.base-dir}")
    private String baseDir;

    private static final String FOLDER_NAME = "achievements";

    public String uploadImage(MultipartFile file) {

        try {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

            if (originalFileName.contains("..")) {
                throw new RuntimeException("Invalid file name");
            }

            String fileName = UUID.randomUUID() + "_" + originalFileName;

            Path uploadPath = Paths.get(baseDir, FOLDER_NAME);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + FOLDER_NAME + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Achievement image upload failed", e);
        }
    }
}