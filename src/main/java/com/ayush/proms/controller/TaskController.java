package com.ayush.proms.controller;

import com.ayush.proms.enums.Status;
import com.ayush.proms.pojos.TaskPojo;
import com.ayush.proms.service.TaskService;
import com.ayush.proms.utils.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController extends BaseController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity createTask(@RequestBody TaskPojo taskPojo){
        Long data = taskService.createTask(taskPojo);
        if (data>=1){
            return new ResponseEntity(successResponse(customMessageSource.get("crud.save",customMessageSource.get("task")),data), HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse(customMessageSource.get("crud.failed",customMessageSource.get("task")),data),HttpStatus.OK);
        }
    }

    @GetMapping("/project-id/{projectId}")
    public ResponseEntity getTaskByProjectId(@PathVariable("projectId") Long projectId){
        List<TaskPojo> data = taskService.getTaskListByProjectId(projectId);
        return new ResponseEntity(successResponse(customMessageSource.get("crud.get_all",customMessageSource.get("task")),data), HttpStatus.OK);
    }

    @GetMapping("/project-id/{projectId}/status/{status}")
    public ResponseEntity getTaskByProjectId(@PathVariable("projectId") Long projectId, @PathVariable("status")Status status){
        List<TaskPojo> data = taskService.getTaskListByProjectIdAndStatus(projectId,status);
        return new ResponseEntity(successResponse(customMessageSource.get("crud.get_all",customMessageSource.get("task")),data), HttpStatus.OK);
    }

    @PutMapping("/change-status")
    public ResponseEntity updateTaskStatus(@RequestBody TaskPojo taskPojo){
        taskService.changeTaskStatus(taskPojo);
        return ResponseEntity.ok(successResponse(customMessageSource.get("status.changed",customMessageSource.get("task")),null));
    }

    @GetMapping("/task-status/project-id/{projectId}")
    public ResponseEntity taskStatus(@PathVariable("projectId") Long projectId){
        int data = taskService.getTaskStatus(projectId);
        return ResponseEntity.ok(successResponse(customMessageSource.get("status.changed",customMessageSource.get("task")),data));
    }
}
