package com.ayush.proms.service.impl;

import com.ayush.proms.enums.DocumentStatus;
import com.ayush.proms.enums.DocumentType;
import com.ayush.proms.enums.MIMEType;
import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.exception.project.ProjectStatusNotValidException;
import com.ayush.proms.model.Document;
import com.ayush.proms.model.Project;
import com.ayush.proms.model.User;
import com.ayush.proms.pojos.DocumentMinimalDetail;
import com.ayush.proms.pojos.DocumentPOJO;
import com.ayush.proms.pojos.ImagePojo;
import com.ayush.proms.repo.DocumentRepo;
import com.ayush.proms.repo.ProjectRepo;
import com.ayush.proms.service.DocumentService;
import com.ayush.proms.service.FileStorageService;
import com.ayush.proms.service.UserService;
import com.ayush.proms.utils.AuthenticationUtil;
import com.ayush.proms.utils.CustomMessageSource;
import com.ayush.proms.utils.EncodeFileToBase64;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final FileStorageService fileStorageService;
    private final DocumentRepo documentRepo;
    private final CustomMessageSource customMessageSource;
    private final AuthenticationUtil authenticationUtil;
    @Autowired
    private ProjectRepo projectRepo;

    public DocumentServiceImpl( FileStorageService fileStorageService, DocumentRepo documentRepo, CustomMessageSource customMessageSource, AuthenticationUtil authenticationUtil) {
        this.fileStorageService = fileStorageService;
        this.documentRepo = documentRepo;
        this.customMessageSource = customMessageSource;
        this.authenticationUtil = authenticationUtil;
    }

    @Override
    public Long upload(DocumentPOJO documentPOJO) throws IOException {
        String fileUrl = fileStorageService.store(documentPOJO.getMultipartFile());
        String extension = FilenameUtils.getExtension(documentPOJO.getMultipartFile().getOriginalFilename());
        List<DocumentType> imageType=Arrays.asList(DocumentType.IMAGE,DocumentType.LOGO);
        List<String> imageExtension=Arrays.asList("jpg","jpeg","png","svg");
        if (imageType.contains(documentPOJO.getDocumentType()) && !imageExtension.contains(extension)){
            throw new RuntimeException("Please upload JPG, JPEG or PNG Image only.");
        }

        if ( ! imageType.contains(documentPOJO.getDocumentType())
         && ! "pdf".equals(extension)) {
                throw new RuntimeException("Please upload pdf file only.");
        }
        if (!DocumentType.IMAGE.equals(documentPOJO.getDocumentType())
                || !DocumentType.LOGO.equals(documentPOJO.getDocumentType())) {
            validDocumentFlow(documentPOJO);
        }

        Document document = toEntity(documentPOJO);
        document.setUrl(fileUrl);
        document.setMimeType(getMIMEType(documentPOJO.getMultipartFile()));
        document.setUrl(fileUrl);
        document.setMimeType(getMIMEType(documentPOJO.getMultipartFile()));
        Optional<Project> projectOptional = projectRepo.findById(documentPOJO.getProjectId());
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            if (DocumentType.LOGO.equals(documentPOJO.getDocumentType())) {
                if (project.getImage() != null){
                    documentRepo.deleteById(project.getImage().getId());
                }
                Long saveDocument = saveDocument(document);
                project.setImage(documentRepo.getById(saveDocument));
                projectRepo.save(project);
                return saveDocument;
            }else if (DocumentType.IMAGE.equals(documentPOJO.getDocumentType())) {
                document.setProject(project);
                Long saveDocument = saveDocument(document);
                return saveDocument;
            } else if (ProjectStatus.ACCEPTED == project.getProjectStatus()) {
                document.setProject(project);
                if (document.getId() != null){
                    Document save = documentRepo.save(document);
                    return save.getId();
                }else {
                    document.setDocumentStatus(DocumentStatus.SUBMITTED);
                    project.setDocumentStatus(documentPOJO.getDocumentType());
                    projectRepo.save(project);
                    return saveDocument(document);
                }
            } else {
                throw new ProjectStatusNotValidException(customMessageSource.get("not.accepted", customMessageSource.get("project")));
            }
        }else{
            throw new RuntimeException(customMessageSource.get("not.found",customMessageSource.get("project")));
        }
    }


    public void validDocumentFlow(DocumentPOJO documentPOJO){
        DocumentType documentType = documentPOJO.getDocumentType();
        Long projectId = documentPOJO.getProjectId();
        if (!documentPOJO.getDocumentType().equals(DocumentType.IMAGE) && documentPOJO.getId() == null) {
            Document uploadedDocument = documentRepo.findDocumentByProjectIdAndStatus(projectId
                    , documentType.name());
            if (uploadedDocument != null) {
                throw new RuntimeException(documentType + " document is already uploaded.");
            }
            DocumentType checkAgainst;
            if (DocumentType.MID_EVALUATION.equals(documentType)) {
                checkAgainst = DocumentType.PROPOSAL;
            } else if (DocumentType.FINAL_DEFENSE.equals(documentType)) {
                checkAgainst = DocumentType.MID_EVALUATION;
            } else {
                return;
            }
            Document document = documentRepo.findDocumentByProjectIdAndStatus(projectId, checkAgainst.name());
            if (document == null) {
                throw new RuntimeException("Upload " + checkAgainst + " document first");
            }
        }
    }



    @Override
    public DocumentPOJO getDocument(Long documentId, String action, HttpServletResponse response) throws IOException {
        //String documentPath = documentRepo.findDocumentPath(documentId);
        Document document = documentRepo.findById(documentId).orElseThrow(() -> new RuntimeException("Document not found."));
        //        String fileName = documentRepo.findFileName(documentId);
//        if (fileName==null){
//            throw new RuntimeException(customMessageSource.get("file.not.found"));
//        }
        File file=new File(document.getUrl());
        Path path=file.toPath();
        //String extension=FilenameUtils.getExtension(fileName);
        if (file.exists()){
            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

//            if (extension.equals("docx") || extension.equals("doc") || extension.equals("pdf") ) {
//                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
//            }
            switch (action) {
                case "view":
                    return DocumentPOJO.builder()
                            .projectId(document.getProject()==null ? null : document.getProject().getId())
                            .encodedFile(EncodeFileToBase64.encodeFileToBase64Binary(file))
                            .id(document.getId())
                            .documentType(document.getDocumentType())
                            .build();
//                case "download":
//                    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"");
//                    break;
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
        User currentUser = authenticationUtil.getCurrentUser();
        return Document.builder()
                .id(documentPOJO.getId()==null ? null: documentPOJO.getId())
                .title(documentPOJO.getTitle() == null ? null : documentPOJO.getTitle())
                .url(documentPOJO.getUrl() == null ? null : documentPOJO.getUrl())
                .documentType(documentPOJO.getDocumentType()==null ? null : documentPOJO.getDocumentType())
                .user(currentUser)
                .build();
    }

    @Override
    public List<DocumentMinimalDetail> getDocumentByProjectId(Long projectId) {
        List<String> documentTypes = Arrays.asList(DocumentType.PROPOSAL.name(), DocumentType.MID_EVALUATION.name(), DocumentType.FINAL_DEFENSE.name());
        List<Document> documentByProjectId = documentRepo.findDocumentByProjectId(projectId,documentTypes);
        return documentByProjectId.stream().
                map(x->new DocumentMinimalDetail(x.getId(),x.getDocumentType())).
                collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Long uploadImages(DocumentPOJO documentPOJO) {
        documentPOJO.getImages().forEach(x->{
            try {
                upload(
                        DocumentPOJO.builder()
                                .multipartFile(x)
                                .projectId(documentPOJO.getProjectId())
                                .documentType(DocumentType.IMAGE)
                                .build()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return 1L;
    }

    @Override
    public List<DocumentPOJO> getImages(Long projectId) throws IOException {
        List<DocumentPOJO> imageList=new ArrayList<>();
        List<ImagePojo> imagesByProjectId = documentRepo.findImagesByProjectId(projectId);
        for (ImagePojo imagePojo: imagesByProjectId){
            DocumentPOJO documentPOJO = getDocument(imagePojo.getImageId(), "view", null);
            imageList.add(documentPOJO);
        }
        return imageList;
    }

    @Override
    public void deleteImage(Long imageId) {
        documentRepo.deleteById(imageId);
    }
}
