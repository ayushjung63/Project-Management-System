package com.ayush.proms.service.impl;

import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.model.Project;
import com.ayush.proms.model.User;
import com.ayush.proms.pojos.ProjectPOJO;
import com.ayush.proms.pojos.UserPOJO;
import com.ayush.proms.repo.ProjectRepo;
import com.ayush.proms.service.DocumentService;
import com.ayush.proms.service.ProjectService;
import com.ayush.proms.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl  implements ProjectService {

    private final ProjectRepo projectRepo;
    private final UserService userService;

    public ProjectServiceImpl(ProjectRepo projectRepo, UserService userService) {
        this.projectRepo = projectRepo;
        this.userService = userService;
    }

    @Override
    public Integer createProject(ProjectPOJO projectPOJO) {
        Project project =toEntity(projectPOJO);
        project.setProjectStatus(ProjectStatus.DRAFT);

        /* Getting Student object from student Id */
        List<UserPOJO> studentList = projectPOJO.getStudentList();
        List<User> users = studentList.stream().map(x -> userService.getUserById(x.getId())).collect(Collectors.toList());
        project.setStudents(users);

        /* Saving project to DB*/
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
    public Long assignSupervisor(Long projectId, Long supervisorId) {
        User supervisor = userService.getUserById(supervisorId);
        Optional<Project> projectOptional = projectRepo.findById(projectId);
        if (projectOptional.isPresent()){
            Project project = projectOptional.get();
            project.setSupervisor(supervisor);
            projectRepo.save(project);
            return project.getId();
        }
        return 0L;
    }

    @Override
    public Project getById(Long projectId) {
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if (optionalProject.isPresent()){
            return optionalProject.get();
        }else{
            return null;
        }
    }

    @Override
    public List<ProjectPOJO> getProjectByStatus(ProjectStatus status) {
        List<Project> projectByStatus = projectRepo.findProjectByStatus(status.toString());
        List<ProjectPOJO> pojoList=new ArrayList<>();
        if (projectByStatus.isEmpty() || projectByStatus==null) {
           return null;
        }else{
            for (Project project :
                    projectByStatus) {
                pojoList.add(toPOJO(project));
            }
            return pojoList;
        }
    }


    @Override
    public List<ProjectPOJO> getAllProjects() {
        List<Project> projectList = projectRepo.findAll();
        List<ProjectPOJO> projectPOJOList = projectList.stream().map(x -> toPOJO(x)).collect(Collectors.toList());
        return projectPOJOList;
    }

    public ProjectPOJO toPOJO(Project project){
        return ProjectPOJO.builder()
                .id(project.getId()==null ? null :project.getId())
                .title(project.getTitle())
                .shortName(project.getShortName())
                .projectTools(project.getProjectTools())
                .studentList(
                        project.getStudents().stream().map(x->userService.toPOJO(x)).collect(Collectors.toList())
                )
                .supervisor(project.getSupervisor()==null ? "Not Assigned " : project.getSupervisor().getFullName())
                .projectStatus(project.getProjectStatus())
                .start_date(project.getStart_date())
                .end_date(project.getEnd_date())
                .build();
    }

    public Project toEntity(ProjectPOJO projectPOJO){
        return Project.builder()
                .id(projectPOJO.getId()==null? null :projectPOJO.getId())
                .title(projectPOJO.getTitle())
                .shortName(projectPOJO.getShortName())
                .projectTools(projectPOJO.getProjectTools())
                .students(
                        projectPOJO.getStudentList().stream().map(x->userService.toEntity(x)).collect(Collectors.toList())
                )
                .projectStatus(projectPOJO.getProjectStatus())
                .start_date(projectPOJO.getStart_date())
                .end_date(projectPOJO.getEnd_date())
                .build();
    }
}
