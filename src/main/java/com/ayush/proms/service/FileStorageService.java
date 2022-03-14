package com.ayush.proms.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FileStorageService {
    String store(MultipartFile file) throws IOException;

    Resource loadAsResource(String fileName) throws MalformedURLException;
}
