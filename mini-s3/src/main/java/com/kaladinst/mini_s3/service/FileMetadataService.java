package com.kaladinst.mini_s3.service;

import com.kaladinst.mini_s3.model.FileMetadata;
import com.kaladinst.mini_s3.repository.FileMetadataRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileMetadataService {
    private final FileMetadataRepository fileMetadataRepository;

    @Value("${mini-s3.storage.location}")
    private String uploadDir;

    @PostConstruct
    public void storageInit() {
        Path storageLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(storageLocation);

        } catch (IOException e) {
            throw new  RuntimeException("Could not initialize storage", e);
        }

    }

    public void store(MultipartFile file) {
        if(!(file.isEmpty())) {
            String contentType = file.getContentType();
            Long size = file.getSize();
            String fileName = file.getOriginalFilename();
            String storedFileName = UUID.randomUUID().toString();
            FileMetadata fileMetadata = new FileMetadata(fileName,storedFileName,contentType,size);

            try {
                Path targetLocation = Paths.get(uploadDir)
                        .resolve(storedFileName)
                        .normalize()
                        .toAbsolutePath();

                Files.copy(file.getInputStream(), targetLocation, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                fileMetadataRepository.save(fileMetadata);

            } catch (IOException e) {
                throw new RuntimeException("Failed to physically store file: " + fileName, e);
            }
        } else {
            throw new IllegalArgumentException("Cannot store empty file");
        }


    }



}
