package com.itgate.springjwt.utils;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {
    private final Path rootLocation = Paths.get("upload");

    public void store(MultipartFile file) {
        try {
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
            String original = name + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
        } catch (Exception e) {
            throw new RuntimeException("Fail");
        }
    }

    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else
                throw new RuntimeException("fail");
        } catch (MalformedURLException e) {
                throw new RuntimeException("fail");
        }


    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());}

    public void init(){
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e){
            throw new RuntimeException("could not intialize storage");
        }
    }
}



