package com.example.fileuploader.filechecksum.services;

import com.example.fileuploader.filechecksum.models.FileChecksumModel;
import com.example.fileuploader.filechecksum.repositories.FileChecksumRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class FileValidationService {

    Logger logger = LoggerFactory.getLogger(FileValidationService.class);

    @Value("${CHECKSUM_HASHING_ALGORITHM}")
    private String checksumHashingAlgorithm;

    @Autowired
    private FileChecksumRepository fileChecksumRepository;

    public boolean fileChecksumExists(File targetFile, int ticketId){
        try {
            String fileChecksum = hashFile(targetFile);
            if(fileChecksumRepository.findByFileChecksum(fileChecksum) == null){
                fileChecksumRepository.save(new FileChecksumModel(ticketId,fileChecksum));

                logger.info("checksum saved for file: " + targetFile.getName() + " with ticket id: " + ticketId);
                return false;
            } else {
                logger.warn("checksum already exists for file: "+ targetFile.getName() + " with ticket id: " + ticketId);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isFileCorrupted(File targetFile, String originalChecksum){
        try {
            String fileChecksum = hashFile(targetFile);
            if(!originalChecksum.equals(fileChecksum)){
                logger.warn("File "+ targetFile.getName() +" is corrupted");
                return true;
            } else{
                logger.info("File "+ targetFile.getName() +" is valid");
                return false;
            }
        } catch (IOException e) {
            return true;
        }
    }

    private String hashFile(File file) throws IOException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(checksumHashingAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        FileInputStream in = new FileInputStream(file);
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        //Read file data and update in message digest
        while ((bytesCount = in.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };
        in.close();
        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        //return complete hash
        return sb.toString();
    }
}
