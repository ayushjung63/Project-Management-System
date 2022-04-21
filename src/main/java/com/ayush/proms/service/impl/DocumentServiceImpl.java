package com.ayush.proms.service.impl;

import com.ayush.proms.enums.DocumentStatus;
import com.ayush.proms.enums.MIMEType;
import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.exception.project.ProjectStatusNotValidException;
import com.ayush.proms.model.Document;
import com.ayush.proms.model.User;
import com.ayush.proms.pojos.DocumentPOJO;
import com.ayush.proms.repo.DocumentRepo;
import com.ayush.proms.service.DocumentService;
import com.ayush.proms.service.FileStorageService;
import com.ayush.proms.service.ProjectService;
import com.ayush.proms.service.UserService;
import com.ayush.proms.utils.CustomMessageSource;
import com.ayush.proms.utils.EncodeFileToBase64;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final DocumentRepo documentRepo;
    private final CustomMessageSource customMessageSource;

    public DocumentServiceImpl(UserService userService, FileStorageService fileStorageService, DocumentRepo documentRepo, CustomMessageSource customMessageSource) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.documentRepo = documentRepo;
        this.customMessageSource = customMessageSource;
    }

    @Override
    public Long upload(DocumentPOJO documentPOJO) throws IOException {
        String fileUrl = fileStorageService.store(documentPOJO.getMultipartFile());
        Document document = toEntity(documentPOJO);
        document.setUrl(fileUrl);
        document.setMimeType(getMIMEType(documentPOJO.getMultipartFile()));
        document.setUrl(fileUrl);
        document.setMimeType(getMIMEType(documentPOJO.getMultipartFile()));
        if (documentPOJO.getType().equals("Image")){
            return saveDocument(document);
        }
        else if (document.getProject().getProjectStatus()== ProjectStatus.ACCEPTED) {
            document.setDocumentStatus(DocumentStatus.SUBMITTED);
            return saveDocument(document);
        }else{
            throw new ProjectStatusNotValidException(customMessageSource.get("not.accepted",customMessageSource.get("project")));
        }
    }

    @Override
    public String getDocument(Long documentId, String action, HttpServletResponse response) throws IOException {
        String documentPath = documentRepo.findDocumentPath(documentId);
        String fileName = documentRepo.findFileName(documentId);
        if (fileName==null){
            throw new RuntimeException(customMessageSource.get("file.not.found"));
        }
        File file=new File(documentPath);
        Path path=file.toPath();
        String extension=FilenameUtils.getExtension(fileName);
        if (file.exists()){
            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            if (extension.equals("docx") || extension.equals("doc") || extension.equals("pdf") ) {
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            }
            switch (action) {
                case "view":
                    return EncodeFileToBase64.encodeFileToBase64Binary(file);
                case "download":
                    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");
                    break;
            }
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } else {
            throw new RuntimeException(customMessageSource.get("file.not.found"));
        }
        return null;
    }

    @Override
    public Long saveDocument(Document document) {
        Document document1 = documentRepo.save(document);
        if (document1 != null) {
            return document1.getId();
        } else {
            throw new ProjectStatusNotValidException(customMessageSource.get("failed.upload",customMessageSource.get("image")));
        }
    }


    private MIMEType getMIMEType(MultipartFile file){
        List<String> docFormat=Arrays.asList("docx","doc");
        List<String> pdfFormat=Arrays.asList("pdf");
        List<String> pptFormat=Arrays.asList("ppt","pptx");
        List<String> imageFormat= Arrays.asList("jpg","jpeg","png");
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (docFormat.contains(extension)){
            return MIMEType.DOCX;
        }else if(docFormat.contains(pdfFormat)){
            return MIMEType.PDF;
        }else if(docFormat.contains(pptFormat)){
            return MIMEType.PPTX;
        }else  if(docFormat.contains(imageFormat)){
            return MIMEType.IMAGE;
        }
        else{
            return MIMEType.OTHERS;
        }
    }

    public Document toEntity(DocumentPOJO documentPOJO){
        User user = userService.getUserById(documentPOJO.getUserId());
        return Document.builder()
                .id(documentPOJO.getId()==null ? null: documentPOJO.getId())
                .title(documentPOJO.getTitle() == null ? null : documentPOJO.getTitle())
                .url(documentPOJO.getUrl() == null ? null : documentPOJO.getUrl())
                .documentType(documentPOJO.getDocumentType()==null ? null : documentPOJO.getDocumentType())
                .user(user==null ? null:user)
                .build();
    }
}
