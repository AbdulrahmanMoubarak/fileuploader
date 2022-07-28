package com.example.fileuploader.utils;

public class FileValidator {

    private String SPECIAL_CHARS = "~ ` ' : ; @ # $ % ^ & * ( ) _ - + = / [ ] { } | , ?";

    public boolean validateFileName(String fileName){
        for (String character:
             this.SPECIAL_CHARS.split(" ")) {
            if(fileName.contains(character)){
                return false;
            }
        }
        return true;
    }
}
