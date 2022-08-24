package com.ayush.proms.repo;

import com.ayush.proms.enums.Status;
import com.ayush.proms.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public interface TaskRepo extends JpaRepository<Task,Long> {

    /*@Query(nativeQuery=true,value="")*/

    @Query(nativeQuery=true,value="select * from task t where t.project_id=?1")
    List<Task> findTaskByProjectId(Long projectId);

    @Query(nativeQuery=true,value="select * from task t where t.project_id=?1 and t.status=?2")
    List<Task> findTaskByProjectIdAndStatus(Long projectId,String status);

    @Transactional
    @Modifying
    @Query(nativeQuery=true,value="update task t set status=?2 where t.id=?1")
    void updateStatus(Long taskId, String status);

    @Query(
            nativeQuery=true,
            value="select count(*) as completedTask from task t where t.project_id=?1 and t.status = 'COMPLETED'"
    )
    double findCompletedTaskByProjectId(Long projectId);


    @Query(
            nativeQuery=true,
            value="select count(*)  from task t where t.project_id=?1"
    )
    double findTotalTaskByProjectId(Long projectId);
}
