package com.ayush.proms.pojos;

import com.ayush.proms.enums.DocumentType;
import com.ayush.proms.model.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentPOJO {
    private Long id;
    private String title;
    private String url;
    private DocumentType documentType;
    private MultipartFile multipartFile;
    private Long userId;
    private Long projectId;
}
