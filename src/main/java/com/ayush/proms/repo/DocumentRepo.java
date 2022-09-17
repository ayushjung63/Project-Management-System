package com.ayush.proms.repo;

import com.ayush.proms.enums.DocumentType;
import com.ayush.proms.model.Document;
import com.ayush.proms.pojos.ImagePojo;
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

    @Query(nativeQuery=true,value="select * from document where project_id=?1 and document_type in (?2)")
    List<Document> findDocumentByProjectId(Long projectId, List<String> documentTypes);

    @Query(nativeQuery=true,value="select * from document where project_id=?1 and document_type=?2")
    Document findDocumentByProjectIdAndStatus(Long projectId, String documentType);

    @Query(nativeQuery=true,value="select * from document where project_id=?1 and document_type=?2")
    void findByProjectIdAndDocumentType(Long projectId, String name);

    @Query(
            nativeQuery=true,
            value="select d.id as imageId , d.url as imagePath  from document d where d.project_id=?1 and d.document_type='IMAGE'"
    )
    List<ImagePojo> findImagesByProjectId(Long projectId);
}
