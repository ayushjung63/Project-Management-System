package com.ayush.proms.controller;

import com.ayush.proms.enums.ProjectStatus;
import com.ayush.proms.pojos.DocumentPOJO;
import com.ayush.proms.pojos.ProjectPOJO;
import com.ayush.proms.service.ProjectService;
import com.ayush.proms.utils.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("project")
@CrossOrigin(origins = "*")
public class ProjectController extends BaseController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity createProject(@ModelAttribute @RequestBody ProjectPOJO projectPOJO) throws IOException {
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

    @GetMapping("/{projectId}")
    public ResponseEntity viewProjectById(@PathVariable("projectId") Long projectId ){
        ProjectPOJO project = projectService.getById(projectId);
        if (project!=null){
            return ResponseEntity.ok(successResponse(customMessageSource.get("crud.get",customMessageSource.get("project")),project));
        }else{
            return new ResponseEntity(errorResponse(customMessageSource.get("not.found",customMessageSource.get("project")),null),HttpStatus.OK);
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

    @GetMapping(value = "/status/{status}")
    public ResponseEntity viewProjectByStatus(@PathVariable("status")ProjectStatus status){
        List<ProjectPOJO> data = projectService.getProjectByStatus(status);
        if (data!=null){
            return ResponseEntity.ok(successResponse(customMessageSource.get("crud.get",customMessageSource.get("project")),data));
        }else {
            return ResponseEntity.ok(errorResponse(customMessageSource.get("crud.failed",customMessageSource.get("project")),data));
        }
    }

    @PostMapping(value = "/upload-image/{projectId}")
    public ResponseEntity uploadProjectLogo(@ModelAttribute DocumentPOJO documentPOJO, @PathVariable("projectId") Long projectId) throws IOException {
        Long data = projectService.uploadImage(documentPOJO, projectId);
        if (data!=null){
            return ResponseEntity.ok(successResponse(customMessageSource.get("success.upload",customMessageSource.get("image")),data));
        }else {
            return ResponseEntity.ok(errorResponse(customMessageSource.get("failed.upload",customMessageSource.get("image")),data));
        }
    }

    @GetMapping(value = "/get-count-by-type")
    public ResponseEntity projectCountWithType() throws IOException {
        List<Map<String, Integer>> data = projectService.getProjectCountByType();
        if (data!=null){
            return ResponseEntity.ok(successResponse(customMessageSource.get("crud.get",customMessageSource.get("project")),data));
        }else{
            return new ResponseEntity(errorResponse(customMessageSource.get("not.found",customMessageSource.get("project")),null),HttpStatus.OK);
        }
    }

    @GetMapping(value = "/get-project-count")
    public ResponseEntity projectCount() throws IOException {
        Integer data = projectService.getTotalProjectCount();
        if (data!=null){
            return ResponseEntity.ok(successResponse(customMessageSource.get("crud.get",customMessageSource.get("project")),data));
        }else{
            return new ResponseEntity(errorResponse(customMessageSource.get("not.found",customMessageSource.get("project")),null),HttpStatus.OK);
        }
    }

    @GetMapping(value = "/get-latest-project")
    public ResponseEntity latestProjects() throws IOException {
        List<ProjectPOJO> data = projectService.getLatestProjects();
        return ResponseEntity.ok(successResponse(customMessageSource.get("crud.get",customMessageSource.get("project")),data));

    }




}
