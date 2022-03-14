package com.ayush.proms.controller;

import com.ayush.proms.pojos.DocumentPOJO;
import com.ayush.proms.service.DocumentService;
import com.ayush.proms.utils.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/document")
public class DocumentController extends BaseController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }


    @PostMapping
    public ResponseEntity uploadDocument(@ModelAttribute @RequestBody DocumentPOJO documentPOJO){
        Long data = null;
        try {
            data = documentService.upload(documentPOJO);
        } catch (IOException e) {
            data=0L;
            e.printStackTrace();
        }
        if (data>=1){
            return new ResponseEntity(successResponse("Document Uploaded Successfully",data), HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse("Failed to Upload Document",data),HttpStatus.OK);
        }
    }
}
