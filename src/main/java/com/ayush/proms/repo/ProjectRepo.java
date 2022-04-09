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

}
