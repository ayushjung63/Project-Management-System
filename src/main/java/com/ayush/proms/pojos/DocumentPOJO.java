package com.ayush.proms.pojos;

import com.ayush.proms.enums.DocumentType;
import java.util.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;



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
    private List<MultipartFile> images;
    private Long userId;
    private Long projectId;
    private String type;

    private String encodedFile;
}
