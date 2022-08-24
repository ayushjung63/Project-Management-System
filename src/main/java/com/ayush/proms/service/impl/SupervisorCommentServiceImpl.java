package com.ayush.proms.service.impl;

import com.ayush.proms.model.Document;
import com.ayush.proms.model.SupervisorComment;
import com.ayush.proms.pojos.SupervisorCommentPOJO;
import com.ayush.proms.repo.SupervisorCommentRepo;
import com.ayush.proms.service.SupervisorCommentService;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupervisorCommentServiceImpl implements SupervisorCommentService {
    private  final SupervisorCommentRepo supervisorCommentRepo;

    public SupervisorCommentServiceImpl(SupervisorCommentRepo supervisorCommentRepo) {
        this.supervisorCommentRepo = supervisorCommentRepo;
    }

    @Override
    public Long createComment(SupervisorCommentPOJO commentPOJO) {
        SupervisorComment supervisorComment = toEntity(commentPOJO);
        SupervisorComment comment = supervisorCommentRepo.save(supervisorComment);
        return comment.getId();
    }

    @Override
    public Long deleteComment(Long commentId) {
        supervisorCommentRepo.deleteById(commentId);
        return 1L;
    }

    @Override
    public List<SupervisorCommentPOJO> getCommentByDocumentId(Long documentId) {
        List<SupervisorComment> comments = supervisorCommentRepo.getByDocumentId(documentId);
        if (comments.size()>0) {
            List<SupervisorCommentPOJO> commentPOJOS = comments.parallelStream().map(x -> toDTO(x)).collect(Collectors.toList());
            return commentPOJOS;
        }
        return Collections.EMPTY_LIST;
    }

    SupervisorComment toEntity(SupervisorCommentPOJO pojo){
        return SupervisorComment.builder()
                .id(pojo.getId()==null ? null : pojo.getId())
                .document(new Document(pojo.getDocumentId()))
                .comment(pojo.getComment())
                .build();
    }

    SupervisorCommentPOJO toDTO(SupervisorComment comment){
        return SupervisorCommentPOJO.builder()
                .id(comment.getId()==null ? null : comment.getId())
                .documentId(comment.getDocument().getId())
                .comment(comment.getComment())
                .build();
    }
}
