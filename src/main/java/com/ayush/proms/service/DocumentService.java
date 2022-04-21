package com.ayush.proms.service;

import com.ayush.proms.model.Document;
import com.ayush.proms.pojos.DocumentPOJO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DocumentService {
    Long upload(DocumentPOJO documentPOJO) throws IOException;

    String getDocument(Long documentId, String action, HttpServletResponse httpServletResponse) throws IOException;

    Long saveDocument(Document document);
}
