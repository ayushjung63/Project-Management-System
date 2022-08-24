package com.ayush.proms.service.impl;

import com.ayush.proms.enums.Status;
import com.ayush.proms.model.Task;
import com.ayush.proms.pojos.TaskPojo;
import com.ayush.proms.repo.ProjectRepo;
import com.ayush.proms.repo.TaskRepo;
import com.ayush.proms.service.TaskService;
import com.ayush.proms.utils.AuthenticationUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepo taskRepo;
    private final ProjectRepo projectRepo;
    private final AuthenticationUtil authenticationUtil;

    public TaskServiceImpl(TaskRepo taskRepo, ProjectRepo projectRepo, AuthenticationUtil authenticationUtil) {
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.authenticationUtil = authenticationUtil;
    }


    @Override
    public Long createTask(TaskPojo taskPojo) {
        Task task = toEntity(taskPojo);
        task.setProject(projectRepo.getById(taskPojo.getProjectId()));
        task.setUser(authenticationUtil.getCurrentUser());
        task.setStatus(Status.PENDING);
        Task save = taskRepo.save(task);
        if (save!=null){
            return save.getId();
        }else {
            return 0L;
        }
    }

    @Override
    public List<TaskPojo> getTaskListByProjectId(Long projectId) {
        List<Task> taskByProjectId = taskRepo.findTaskByProjectId(projectId);
        if (taskByProjectId!=null){
            return taskByProjectId.parallelStream()
                    .map(x->toDto(x)).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<TaskPojo> getTaskListByProjectIdAndStatus(Long projectId, Status status) {
        List<Task> taskByProjectId = taskRepo.findTaskByProjectIdAndStatus(projectId,String.valueOf(status));
        if (taskByProjectId!=null){
            return taskByProjectId.parallelStream()
                    .map(x->toDto(x)).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void changeTaskStatus(TaskPojo taskPojo) {
        Optional<Task> taskOptional = taskRepo.findById(taskPojo.getId());
        if (taskOptional.isPresent()){
            taskRepo.updateStatus(taskPojo.getId(),String.valueOf(taskPojo.getStatus()));
        }else{
            throw new RuntimeException("Task status cannot be changed.");
        }
    }

    Task toEntity(TaskPojo taskPojo){
            return Task.builder()
                    .id(taskPojo.getId()==null ? null : taskPojo.getId())
                    .title(taskPojo.getTitle())
                    .description(taskPojo.getDescription())
                    .build();
    }

    TaskPojo toDto(Task task){
        return TaskPojo.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .projectId(task.getProject().getId())
                .addedBy(task.getUser().getFullName())
                .build();
    }

    @Override
    public int getTaskStatus(Long projectId) {
        double completedTask = taskRepo.findCompletedTaskByProjectId(projectId);
        double totalTask = taskRepo.findTotalTaskByProjectId(projectId);
        if (totalTask ==0 || completedTask==0){
            return 0;
        }

        double v =( completedTask / totalTask ) * 100;
        int data = (int) v;
        return data;
    }
}
