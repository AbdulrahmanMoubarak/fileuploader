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
    private String checksumAlgorithm;

    @Autowired
    private FileChecksumRepository fileChecksumRepository;

    public boolean fileChecksumExists(File file, int ticketId){
        try {
            String fileChecsum = hashFile(file);
            if(fileChecksumRepository.findByChecksum(fileChecsum) == null){
                fileChecksumRepository.save(new FileChecksumModel(ticketId,fileChecsum));

                logger.info("checksum saved for file: " + file.getName() + " with ticket id: " + ticketId);
                return false;
            } else {
                logger.warn("checksum already exists for file: "+ file.getName() + " with ticket id: " + ticketId);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean fileCorrupted(File file, String checksum){
        try {
            String fileChecsum = hashFile(file);
            if(!checksum.equals(fileChecsum)){
                logger.warn("File "+ file.getName() +" is corrupted");
                return true;
            } else{
                logger.info("File "+ file.getName() +" is valid");
                return false;
            }
        } catch (IOException e) {
            return true;
        }
    }

    private String hashFile(File file) throws IOException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(checksumAlgorithm);
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
