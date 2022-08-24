package com.ayush.proms.pojos;

import com.ayush.proms.enums.DocumentType;
import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.model.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectPOJO {
    private Long id;
    private String title;
    private String description;
    private String shortName;
    private Date startDate;
    private Date endDate;
    private List<String> projectTools=new ArrayList<>();
    private List<MinimalDetail> studentList=new ArrayList<>();
    private String supervisor;
    private ProjectStatus projectStatus;
    private Long imageId;
    private DocumentType documentStatus;
    private String projectType;
    private Long[] studentIds;

    private MultipartFile image;

}
