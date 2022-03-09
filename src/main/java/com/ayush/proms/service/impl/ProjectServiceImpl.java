package com.ayush.proms.service.impl;

import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.model.Project;
import com.ayush.proms.pojos.ProjectPOJO;
import com.ayush.proms.repo.ProjectRepo;
import com.ayush.proms.service.ProjectService;
import com.ayush.proms.utils.ObjectConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl  implements ProjectService {

    private final ProjectRepo projectRepo;
    private final ObjectConverter objectConverter;

    public ProjectServiceImpl(ProjectRepo projectRepo, ObjectConverter objectConverter) {
        this.projectRepo = projectRepo;
        this.objectConverter = objectConverter;
    }

    @Override
    public Integer createProject(ProjectPOJO projectPOJO) {
        Project project = objectConverter.convertProjectPojoToEntity(projectPOJO);
        project.setProjectStatus(ProjectStatus.DRAFT);
        Project data = projectRepo.save(project);
        if (data!=null){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public Integer approveProject(Long projectId) {
        Project projectToApprove = getById(projectId);
        if (projectToApprove.getProjectStatus()==ProjectStatus.DRAFT) {
            projectToApprove.setProjectStatus(ProjectStatus.ACCEPTED);
            Project data = projectRepo.save(projectToApprove);
            if (data != null) {
                return 1;
            } else {
                return 0;
            }
        }else{
            return 0;
        }
    }

    @Override
    public Integer rejectProject(Long projectId) {
        Project projectToReject = getById(projectId);
        if (projectToReject.getProjectStatus()==ProjectStatus.DRAFT) {
            projectToReject.setProjectStatus(ProjectStatus.REJECTED);
            Project data = projectRepo.save(projectToReject);
            if (data != null) {
                return 1;
            } else {
                return 0;
            }
        }else{
            return 0;
        }
    }

    @Override
    public Project getById(Long projectId) {
        return projectRepo.findById(projectId).get();
    }

    @Override
    public List<ProjectPOJO> getAllProjects() {
        List<Project> projectList = projectRepo.findAll();
        List<ProjectPOJO> projectPOJOList = projectList.stream().map(x -> objectConverter.convertProjectEntityToPojo(x)).collect(Collectors.toList());
        return projectPOJOList;
    }
}
