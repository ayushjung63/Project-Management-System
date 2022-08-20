package com.ayush.proms.repo;

import com.ayush.proms.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface DocumentRepo extends JpaRepository<Document,Long> {

    @Query(value="select d.url from document d where d.id=?1",nativeQuery=true)
    String findDocumentPath(Long documentId);

    @Query(value="select d.title from document d where d.id=?1",nativeQuery=true)
    String findFileName(Long documentId);

    @Query(nativeQuery=true,value="select * from document where project_id=?1")
    List<Document> findDocumentByProjectId(Long projectId);

    @Query(nativeQuery=true,value="select * from document where project_id=?1 and document_type=?2")
    List<Document> findDocumentByProjectIdAndStatus(Long projectId, String documentType);
}
