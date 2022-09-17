package com.ayush.proms.repo;

import com.ayush.proms.model.SupervisorComment;
import com.ayush.proms.pojos.SupervisorLogPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface SupervisorCommentRepo extends JpaRepository<SupervisorComment,Long> {

    @Query(nativeQuery=true,value=" select * from supervisor_comment where document_id = ?1")
    List<SupervisorComment> getByDocumentId(Long documentId);

    @Query(
            nativeQuery=true,
            value="select sc.id as supervisorCommentId,u.full_name as supervisorName,sc.comment as comment,date(sc.created_date) as reviewedDate from supervisor_comment sc\n" +
                    "    inner join users u on sc.created_by=u.id\n" +
                    "    inner join document d on sc.document_id = d.id\n" +
                    "    inner join project p on p.id=d.project_id and p.id= ?1"
    )
    List<SupervisorLogPojo> findSupervisorLogByProjectId(Long projectId);
}
