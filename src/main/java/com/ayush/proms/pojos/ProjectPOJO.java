package com.ayush.proms.pojos;

import com.ayush.proms.enums.DocumentType;
import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.model.User;
import lombok.*;

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
    private Date start_date;
    private Date end_date;
    private List<String> projectTools=new ArrayList<>();
    private List<MinimalDetail> studentList;
    private String supervisor;
    private ProjectStatus projectStatus;
    private Long imageId;
    private DocumentType documentStatus;
    private String projectType;
    private Long[] studentIds;

}
