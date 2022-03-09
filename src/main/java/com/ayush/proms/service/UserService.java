package com.ayush.proms.service;

import com.ayush.proms.pojos.UserPOJO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    Integer createUser(UserPOJO userPOJO);

    Integer importUser(MultipartFile file);

}
