package com.kaladinst.mini_s3.controller;

import com.kaladinst.mini_s3.model.FileMetadata;
import com.kaladinst.mini_s3.service.FileMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/download/{storedFilename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String storedFilename ) {
        try {
            Resource fileResource = fileMetadataService.loadAsResource(storedFilename);
            FileMetadata metadata = fileMetadataService.getmetadata(storedFilename);

            String headerValue = "attachment; filename=\"" + metadata.getOriginalFilename()+ "\"";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(metadata.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(fileResource);
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
