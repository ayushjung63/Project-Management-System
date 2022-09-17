package com.ayush.proms.service.impl;

import com.ayush.proms.enums.DocumentType;
import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.enums.ProjectType;
import com.ayush.proms.enums.Role;
import com.ayush.proms.mail.Email;
import com.ayush.proms.mail.EmailSender;
import com.ayush.proms.model.Document;
import com.ayush.proms.model.Project;
import com.ayush.proms.model.User;
import com.ayush.proms.pojos.DocumentPOJO;
import com.ayush.proms.pojos.MinimalDetail;
import com.ayush.proms.pojos.ProjectPOJO;
import com.ayush.proms.repo.ProjectRepo;
import com.ayush.proms.service.DocumentService;
import com.ayush.proms.service.ProjectService;
import com.ayush.proms.service.UserService;
import com.ayush.proms.utils.AuthenticationUtil;
import com.ayush.proms.utils.CustomMessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.ayush.proms.enums.Role.ADMIN;

@Service
public class ProjectServiceImpl  implements ProjectService {

    private final ProjectRepo projectRepo;
    private final UserService userService;
    private final CustomMessageSource customMessageSource;
    private final AuthenticationUtil authenticationUtil;
    private final EmailSender emailSender;
    private final DocumentService documentService;

    public ProjectServiceImpl(ProjectRepo projectRepo, UserService userService, DocumentService documentService, CustomMessageSource customMessageSource, AuthenticationUtil authenticationUtil, EmailSender emailSender, DocumentService documentService1) {
        this.projectRepo = projectRepo;
        this.userService = userService;
        this.customMessageSource = customMessageSource;
        this.authenticationUtil = authenticationUtil;
        this.emailSender = emailSender;
        this.documentService = documentService1;
    }

    @Override
    public Integer createProject(ProjectPOJO projectPOJO) throws IOException {
        Project project =toEntity(projectPOJO);
        project.setProjectStatus(ProjectStatus.DRAFT);

        /* Getting Student object from student Id */
        User currentUser = authenticationUtil.getCurrentUser();

        /*List<MinimalDetail> studentList = projectPOJO.getStudentList();*/


        List<User> studentList=new ArrayList<>();
        if (projectPOJO.getStudentIds() != null) {
            for (Long studentId : projectPOJO.getStudentIds()) {
                studentList.add(userService.getUserById(studentId));
            }
        }
        if (currentUser.getRole()==Role.STUDENT){
            User userById = userService.getUserById(currentUser.getId());
            studentList.add(userById);
        }
        project.setStudents(studentList);

        if(projectPOJO.getImage() != null) {
            Long imageId = documentService.upload(
                    DocumentPOJO.builder()
                            .multipartFile(projectPOJO.getImage())
                            .build()
            );
            project.setImage(new Document(imageId));
        }
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
        ProjectPOJO projectToApprove = getById(projectId);
        if (projectToApprove.getProjectStatus()==ProjectStatus.DRAFT) {
            projectToApprove.setProjectStatus(ProjectStatus.ACCEPTED);
            Project data = projectRepo.save(toEntity(projectToApprove));
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
        ProjectPOJO projectToReject = getById(projectId);
        if (projectToReject.getProjectStatus()==ProjectStatus.DRAFT) {
            projectToReject.setProjectStatus(ProjectStatus.REJECTED);
            Project data = projectRepo.save(toEntity(projectToReject));
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
            project.setProjectStatus(ProjectStatus.ACCEPTED);
            projectRepo.save(project);
            emailSender.sendSupervisorAssignedMail(
                    Email.builder()
                            .to(supervisor.getEmail())
                            .body("" +
                                    "Dear, "+supervisor.getFullName()+"\n" +
                                    "You have been assigned to "+ project.getTitle()+" ." +
                                    "Please visit ProMIS for more details."
                            )
                            .build()
            );
            return project.getId();
        }
        return 0L;
    }

    @Override
    public ProjectPOJO getById(Long projectId) {
        Optional<Project> optionalProject = projectRepo.findById(projectId);
        if (optionalProject.isPresent()){
            return toPOJO(optionalProject.get());
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
    public Long uploadImage(DocumentPOJO documentPOJO, Long projectId) throws IOException {
        Optional<Project> projectOptional = projectRepo.findById(projectId);
        if (projectOptional.isPresent()){
           documentPOJO.setDocumentType(documentPOJO.getDocumentType()==null? DocumentType.LOGO: documentPOJO.getDocumentType());
            Long upload = documentService.upload(documentPOJO);
            Project project = projectOptional.get();
                return project.getId();

        }else{
            throw new RuntimeException(customMessageSource.get("not.found",customMessageSource.get("project")));
        }
    }


    @Override
    public List<ProjectPOJO> getAllProjects() {
        User currentUser = authenticationUtil.getCurrentUser();
        List<Project> projectList=new ArrayList<>();
        if (currentUser.getRole()== ADMIN) {
            projectList = projectRepo.findAll();
        }else if(Role.SUPERVISOR == currentUser.getRole()){
            projectList=projectRepo.findProjectForSupervisor(currentUser.getId());
        }else if(Role.STUDENT == currentUser.getRole()){
            projectList=projectRepo.findProjectForStudent(currentUser.getId());
        }else{
            return new ArrayList<>();
        }
        List<ProjectPOJO> projectPOJOList = projectList.stream().map(x -> toPOJO(x)).collect(Collectors.toList());
        return projectPOJOList;
    }

    @Override
    public List<Map<String, Integer>> getProjectCountByType() {
        List<Map<String, Integer>> projectCountByType = projectRepo.findProjectCountByType();
        return projectCountByType;
    }

    @Override
    public Integer getTotalProjectCount() {
        User currentUser = authenticationUtil.getCurrentUser();
        Long currentUserId = currentUser.getId();

        Integer projectCount=0;
        if (Role.STUDENT.equals(currentUser.getRole())){
            projectCount=projectRepo.findProjectCountByStudentId(currentUserId);
        }else if(Role.SUPERVISOR.equals(currentUser.getRole())){
            projectCount=projectRepo.findProjectCountBySupervisorId(currentUserId);
        }else{
            projectCount=projectRepo.findTotalProjectCount();
        }
        return projectCount;
    }

    @Override
    public List<ProjectPOJO> getLatestProjects() {
        List<Project> latestProjects = projectRepo.findLatestProjects();
        if (latestProjects!=null){
            List<ProjectPOJO> projectPOJOList = latestProjects.stream().map(x -> toPOJO(x)).collect(Collectors.toList());
            return projectPOJOList;
        }else{
            return Collections.EMPTY_LIST;
        }

    }

    public ProjectPOJO toPOJO(Project project){
        return ProjectPOJO.builder()
                .id(project.getId()==null ? null :project.getId())
                .title(project.getTitle())
                .description(project.getDescription()==null ? null :project.getDescription())
                .shortName(project.getShortName())
                .projectTools(project.getProjectTools())
                .studentList(
                        project.getStudents().stream().map(x->new MinimalDetail(x.getId(),x.getFullName())).collect(Collectors.toList())
                )
                .supervisor(project.getSupervisor()==null ? "Not Assigned" : project.getSupervisor().getFullName())
                .projectStatus(project.getProjectStatus())
                .startDate(project.getStart_date())
                .endDate(project.getEnd_date())
                .imageId(project.getImage()==null ? null : project.getImage().getId())
                .documentStatus(project.getDocumentStatus())
                .projectType(String.valueOf(project.getProjectType()))
                .build();
    }

    public Project toEntity(ProjectPOJO projectPOJO){
        return Project.builder()
                .id(projectPOJO.getId()==null? null :projectPOJO.getId())
                .title(projectPOJO.getTitle())
                .description(projectPOJO.getDescription()==null ? null :projectPOJO.getDescription())
                .shortName(projectPOJO.getShortName())
                .projectTools(projectPOJO.getProjectTools())
                .start_date(projectPOJO.getStartDate())
                .end_date(projectPOJO.getEndDate())
                .projectType(ProjectType.valueOf(projectPOJO.getProjectType()))
                .build();
    }

    @Override
    public void deleteProject(Long projectId) {
        Project project = projectRepo.findById(projectId).orElseThrow(
                () -> new RuntimeException("Project not found")
        );
        projectRepo.deleteById(projectId);
    }
}
