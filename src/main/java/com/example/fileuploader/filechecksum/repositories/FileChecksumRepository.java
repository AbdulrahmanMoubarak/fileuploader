package com.example.fileuploader.filechecksum.repositories;

import com.example.fileuploader.filechecksum.models.FileChecksumModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileChecksumRepository extends JpaRepository<FileChecksumModel, Integer> {
    FileChecksumModel findByFileChecksum(String checksum);
}
