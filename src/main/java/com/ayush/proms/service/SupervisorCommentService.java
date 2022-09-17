package com.ayush.proms.service;

import com.ayush.proms.pojos.SupervisorCommentPOJO;
import com.ayush.proms.pojos.SupervisorLogPojo;

import java.util.*;

public interface SupervisorCommentService {
    Long createComment(SupervisorCommentPOJO commentPOJO);
    void deleteComment(Long commentId);

    List<SupervisorCommentPOJO>  getCommentByDocumentId(Long documentId);

    List<SupervisorLogPojo> getSupervisorLog(Long projectId);
}
