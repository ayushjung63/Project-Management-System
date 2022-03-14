package com.ayush.proms.controller;

import com.ayush.proms.model.Project;
import com.ayush.proms.pojos.ProjectPOJO;
import com.ayush.proms.service.ProjectService;
import com.ayush.proms.utils.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity createProject(@RequestBody ProjectPOJO projectPOJO){
        Integer data = projectService.createProject(projectPOJO);
        if (data>=1){
            return new ResponseEntity(successResponse("Project Submitted Successfully",data),HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse("Project Submit Unsuccessfull",data),HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity viewAllProject(){
        List<ProjectPOJO> allProjects = projectService.getAllProjects();
        if (allProjects.isEmpty() || allProjects==null){
            return new ResponseEntity(errorResponse("No Projeect found",null),HttpStatus.OK);
        }else{
            return new ResponseEntity(successResponse("Project fetched Successfully",allProjects),HttpStatus.OK);
        }
    }

    @GetMapping(value = "/reject-project/{projectId}")
    public ResponseEntity rejectProject(@PathVariable("projectId") Long projectId){
        Integer data = projectService.rejectProject(projectId);
        if (data>=1){
            return new ResponseEntity(successResponse("Project Rejected Successfully",data),HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse("Failed to reject Project",data),HttpStatus.OK);
        }
    }

    @GetMapping(value = "/approve-project/{projectId}")
    public ResponseEntity approveProject(@PathVariable("projectId") Long projectId){
        Integer data = projectService.approveProject(projectId);
        if (data>=1){
            return new ResponseEntity(successResponse("Project Approve Successfully",data),HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse("Failed to approve Project",data),HttpStatus.OK);
        }
    }

    @GetMapping (value = "/{projectId}/assign-supervisor/{supervisorId}")
    public ResponseEntity assignSupervisor(@PathVariable("projectId") Long projectId,@PathVariable("supervisorId") Long supervisorId){
        Long data = projectService.assignSupervisor(projectId,supervisorId);
        if (data>=1){
            return new ResponseEntity(successResponse("Project Supervisor Assigned Successfully",data),HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse("Failed to assign supervisor",data),HttpStatus.OK);
        }
    }


}
