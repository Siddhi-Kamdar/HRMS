
package com.example.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class ExpenseProofService {

    @Value("${file.upload.base-dir}")
    private String baseDir;

    public String uploadProof(MultipartFile file) {

        try {

            String folderPath =
                    baseDir + File.separator + "expense-proofs";

            File directory = new File(folderPath);

            if (!directory.exists())
                directory.mkdirs();

            String fileName =
                    UUID.randomUUID() + "_" +
                            file.getOriginalFilename();

            String fullPath =
                    folderPath + File.separator + fileName;

            file.transferTo(new File(fullPath));

            return "uploads/expense-proofs/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Expense proof upload failed");
        }
    }
}