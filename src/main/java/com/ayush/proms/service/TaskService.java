package com.ayush.proms.service;

import com.ayush.proms.enums.Status;
import com.ayush.proms.pojos.TaskPojo;
import java.util.*;

public interface TaskService {

    Long createTask(TaskPojo taskPojo);

    List<TaskPojo> getTaskListByProjectId(Long projectId);

    List<TaskPojo> getTaskListByProjectIdAndStatus(Long projectId, Status status);

    void changeTaskStatus(TaskPojo taskPojo);

    int getTaskStatus(Long projectId);
}
