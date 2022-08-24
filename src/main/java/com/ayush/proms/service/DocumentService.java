package com.ayush.proms.service;

import com.ayush.proms.model.Document;
import com.ayush.proms.pojos.DocumentMinimalDetail;
import com.ayush.proms.pojos.DocumentPOJO;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DocumentService {
    Long upload(DocumentPOJO documentPOJO) throws IOException;

    DocumentPOJO getDocument(Long documentId, String action, HttpServletResponse httpServletResponse) throws IOException;

    Long saveDocument(Document document);

    List<DocumentMinimalDetail> getDocumentByProjectId(Long projectId);
}
