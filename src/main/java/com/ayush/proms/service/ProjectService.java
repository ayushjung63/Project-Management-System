package com.ayush.proms.service;

import com.ayush.proms.model.Project;
import com.ayush.proms.pojos.DocumentPOJO;
import com.ayush.proms.pojos.ProjectPOJO;

import java.util.*;

public interface ProjectService {

    List<ProjectPOJO> getAllProjects();

    Integer createProject(ProjectPOJO projectPOJO);

    Integer approveProject(Long projectId);

    Integer rejectProject(Long projectId);

    Long assignSupervisor(Long projectId, Long supervisorId);

    Project getById(Long projectId);

}
