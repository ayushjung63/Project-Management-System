package com.ayush.proms.service;

import com.ayush.proms.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ExcelService {
    public List<User> convertToEntity(InputStream inputStream);
}
