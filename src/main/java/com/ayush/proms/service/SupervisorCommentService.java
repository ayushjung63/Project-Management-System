package com.ayush.proms.service;

import com.ayush.proms.pojos.SupervisorCommentPOJO;
import java.util.*;

public interface SupervisorCommentService {
    Long createComment(SupervisorCommentPOJO commentPOJO);
    Long deleteComment(Long commentId);

    List<SupervisorCommentPOJO>  getCommentByDocumentId(Long documentId);
}
