package com.ecommerce.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        //Get the file name of current/original file
        String originalFileName= file.getOriginalFilename();

        //Generate a unique file name
        String randomId= UUID.randomUUID().toString();
        String newFileName=randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath=path+ File.separator+newFileName;

        //check if path exist or create
        File folder=new File(path);
        if(!folder.exists())
            folder.mkdir();

        //Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        //return file name
        return  newFileName;
    }

}
