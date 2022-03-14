package com.ayush.proms.service.impl;

import com.ayush.proms.enums.DocumentStatus;
import com.ayush.proms.enums.MIMEType;
import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.exception.project.ProjectStatusNotValidException;
import com.ayush.proms.model.Document;
import com.ayush.proms.model.Project;
import com.ayush.proms.model.User;
import com.ayush.proms.pojos.DocumentPOJO;
import com.ayush.proms.repo.DocumentRepo;
import com.ayush.proms.service.DocumentService;
import com.ayush.proms.service.FileStorageService;
import com.ayush.proms.service.ProjectService;
import com.ayush.proms.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.io.IOException;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final UserService userService;
    private final ProjectService projectService;
    private final FileStorageService fileStorageService;
    private final DocumentRepo documentRepo;

    public DocumentServiceImpl(UserService userService, ProjectService projectService, FileStorageService fileStorageService, DocumentRepo documentRepo) {
        this.userService = userService;
        this.projectService = projectService;
        this.fileStorageService = fileStorageService;
        this.documentRepo = documentRepo;
    }

    @Override
    public Long upload(DocumentPOJO documentPOJO) throws IOException {
        String fileUrl = fileStorageService.store(documentPOJO.getMultipartFile());
        Document document = toEntity(documentPOJO);
        if (document.getProject().getProjectStatus()== ProjectStatus.ACCEPTED) {
            document.setUrl(fileUrl);
            document.setMimeType(getMIMEType(documentPOJO.getMultipartFile()));
            document.setDocumentStatus(DocumentStatus.SUBMITTED);


            Document document1 = documentRepo.save(document);
            if (document1 != null) {
                return document1.getId();
            } else {
                return 0L;
            }
        }else{
            throw new ProjectStatusNotValidException("Project has not been accepted.");
        }
    }


    private MIMEType getMIMEType(MultipartFile file){
        List<String> docFormat=Arrays.asList("docx","doc");
        List<String> pdfFormat=Arrays.asList("pdf");
        List<String> pptFormat=Arrays.asList("ppt","pptx");
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (docFormat.contains(extension)){
            return MIMEType.DOCX;
        }else if(docFormat.contains(pdfFormat)){
            return MIMEType.PDF;
        }else if(docFormat.contains(pptFormat)){
            return MIMEType.PPTX;
        }else{
            return MIMEType.OTHERS;
        }
    }

    public Document toEntity(DocumentPOJO documentPOJO){
        Project project = projectService.getById(documentPOJO.getProjectId());
        User user = userService.getUserById(documentPOJO.getUserId());
        return Document.builder()
                .id(documentPOJO.getId()==null ? null: documentPOJO.getId())
                .title(documentPOJO.getTitle())
                .url(documentPOJO.getUrl())
                .documentType(documentPOJO.getDocumentType())
                .user(user==null ?null:user)
                .project(project==null ? null : project)
                .build();
    }
}
