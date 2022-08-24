package com.ayush.proms.repo;

import com.ayush.proms.model.SupervisorComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface SupervisorCommentRepo extends JpaRepository<SupervisorComment,Long> {

    @Query(nativeQuery=true,value=" select * from supervisor_comment where document_id = ?1")
    List<SupervisorComment> getByDocumentId(Long documentId);
}
