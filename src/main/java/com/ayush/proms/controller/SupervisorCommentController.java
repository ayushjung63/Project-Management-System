package com.ayush.proms.controller;

import com.ayush.proms.pojos.ProjectPOJO;
import com.ayush.proms.pojos.SupervisorCommentPOJO;
import com.ayush.proms.service.SupervisorCommentService;
import com.ayush.proms.utils.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/supervisor-comment")
public class SupervisorCommentController extends BaseController {
    private  final SupervisorCommentService supervisorCommentService;

    public SupervisorCommentController(SupervisorCommentService supervisorCommentService) {
        this.supervisorCommentService = supervisorCommentService;
    }

    @GetMapping(value = "/get-latest-comment/document-id/{id}")
    public ResponseEntity latestComments(@PathVariable("id") Long documentId) throws IOException {
        return ResponseEntity.ok(successResponse(customMessageSource.get("crud.get",customMessageSource.get("comment"))
                ,        supervisorCommentService.getCommentByDocumentId(documentId)));
    }

    @PostMapping()
    public ResponseEntity latestComments(@RequestBody SupervisorCommentPOJO pojo) throws IOException {
        Long comment = supervisorCommentService.createComment(pojo);
        return ResponseEntity.ok(successResponse(customMessageSource.get("crud.get",customMessageSource.get("comment"))
                ,comment));
    }
}
