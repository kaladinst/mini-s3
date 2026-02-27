package com.kaladinst.mini_s3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.naming.Name;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "file_metadata")
@NoArgsConstructor
@Getter
@Setter
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false, unique = true)
    private String storedFilename;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long sizeInBytes;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    public FileMetadata(String originalFilename, String storedFilename, String contentType, Long sizeInBytes) {
        this.contentType = contentType;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.uploadDate = LocalDateTime.now();
        this.sizeInBytes = sizeInBytes;
    }


}

