package com.ayush.proms.service.impl;

import com.ayush.proms.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private String path = System.getProperty("user.home");
    String filePath = path + File.separator + "uploads";

    @Override
    public String store(MultipartFile file) throws IOException {

        File directory = new File(filePath);
        String fileName= StringUtils.cleanPath(file.getOriginalFilename());

        if(!directory.exists()){
            directory.mkdir();
        }

        Path filePaths = Paths.get(filePath + File.separator + fileName);

        Files.copy(file.getInputStream(), filePaths, StandardCopyOption.REPLACE_EXISTING);
        return filePath +"/"+ fileName;
    }

    @Override
    public Resource loadAsResource(String fileName) throws MalformedURLException {
        return null;
    }
}
