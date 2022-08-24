package com.ayush.proms.service;

import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.model.Project;
import com.ayush.proms.pojos.DocumentPOJO;
import com.ayush.proms.pojos.ProjectPOJO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public interface ProjectService {

    List<ProjectPOJO> getAllProjects();

    Integer createProject(ProjectPOJO projectPOJO) throws IOException;

    Integer approveProject(Long projectId);

    Integer rejectProject(Long projectId);

    Long assignSupervisor(Long projectId, Long supervisorId);

    ProjectPOJO getById(Long projectId);

    List<ProjectPOJO> getProjectByStatus(ProjectStatus status);

    Long uploadImage(DocumentPOJO documentPOJO, Long projectId) throws IOException;

    List<Map<String,Integer>> getProjectCountByType();

    Integer getTotalProjectCount();

    List<ProjectPOJO> getLatestProjects();

    void deleteProject(Long projectId);


}
