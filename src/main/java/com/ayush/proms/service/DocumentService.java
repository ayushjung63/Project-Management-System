package com.ayush.proms.service;

import com.ayush.proms.pojos.DocumentPOJO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentService {
    Long upload(DocumentPOJO documentPOJO) throws IOException;
}
