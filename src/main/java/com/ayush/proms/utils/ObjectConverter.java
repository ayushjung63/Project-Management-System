package com.ayush.proms.utils;

import com.ayush.proms.model.Project;
import com.ayush.proms.model.User;
import com.ayush.proms.pojos.ProjectPOJO;
import com.ayush.proms.pojos.UserPOJO;
import org.springframework.stereotype.Component;

@Component
public class ObjectConverter {

    public ProjectPOJO convertProjectEntityToPojo(Project project){
        return ProjectPOJO.builder()
                .id(project.getId()==null ? null :project.getId())
                .title(project.getTitle())
                .shortName(project.getShortName())
                .projectTools(project.getProjectTools())
                .students(project.getStudents())
                .supervisor(project.getSupervisor())
                .projectStatus(project.getProjectStatus())
                .start_date(project.getStart_date())
                .end_date(project.getEnd_date())
                .build();
    }

    public Project convertProjectPojoToEntity(ProjectPOJO projectPOJO){
        return Project.builder()
                .id(projectPOJO.getId()==null? null :projectPOJO.getId())
                .title(projectPOJO.getTitle())
                .shortName(projectPOJO.getShortName())
                .projectTools(projectPOJO.getProjectTools())
                .students(projectPOJO.getStudents())
                .supervisor(projectPOJO.getSupervisor())
                .projectStatus(projectPOJO.getProjectStatus())
                .start_date(projectPOJO.getStart_date())
                .end_date(projectPOJO.getEnd_date())
                .build();
    }

    public User convertUserPojoToEntity(UserPOJO userPOJO){
        return User.builder()
                .id(userPOJO.getId())
                .fullName(userPOJO.getFullName())
                .email(userPOJO.getEmail())
                .faculty(userPOJO.getFaculty())
                .semester(userPOJO.getSemester())
                .projects(userPOJO.getProjects())
                .build();
    }

    public UserPOJO convertUserEntityToPojo(User user){
        return UserPOJO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .faculty(user.getFaculty())
                .semester(user.getSemester())
                .projects(user.getProjects())
                .build();
    }
}
