package com.ayush.proms.repo;

import com.ayush.proms.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ProjectRepo extends JpaRepository<Project,Long> {

    @Query(value="select * from Project p where p.project_status=?1",nativeQuery=true)
    List<Project> findProjectByStatus(String status);

    @Query(nativeQuery=true,value="select * from project p where p.id in (select project_id from project_users where user_id =?1)")
    List<Project> findProjectForStudent(Long studentId);

    @Query(nativeQuery=true,value="select * from project p where p.supervisor_id=?1")
    List<Project> findProjectForSupervisor(Long studentId);

    @Query(value="select p.project_type as type,cast(count(p.id) as numeric) as totalProject from project p  group by p.project_type",nativeQuery=true)
    List<Map<String,Integer>> findProjectCountByType();

    @Query(nativeQuery=true,value="select count(p.id) from project p inner join project_users pu on p.id = pu.project_id where pu.user_id=?1")
    Integer findProjectCountByStudentId(Long studentId);

    @Query(nativeQuery=true,value="select count(p.id) from project p where p.supervisor_id=?1")
    Integer findProjectCountBySupervisorId(Long studentId);

    @Query(nativeQuery=true,value="select count(p.id) from project p")
    Integer findTotalProjectCount();

    @Query(nativeQuery=true,value="select * from project order by id desc limit 7;")
    List<Project> findLatestProjects();
}
