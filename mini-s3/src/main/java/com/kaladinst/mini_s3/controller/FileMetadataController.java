package com.kaladinst.mini_s3.controller;

import com.kaladinst.mini_s3.service.FileMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileMetadataController {
    private final FileMetadataService fileMetadataService;

    @PostMapping("/upload")
    public ResponseEntity<String> statusShow(@RequestParam("file") MultipartFile file) {
        try{
            fileMetadataService.store(file);
            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save file.");
        }

    }


}
