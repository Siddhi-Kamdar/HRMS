package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.beans.factory.annotation.Value;

@CrossOrigin(origins = "http://localhost:5173")
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("why why does it not work ? :)");
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir);
        System.out.println("file:" + uploadDir);
    }
}