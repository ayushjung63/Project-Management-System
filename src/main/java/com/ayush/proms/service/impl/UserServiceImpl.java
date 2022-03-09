package com.ayush.proms.service.impl;

import com.ayush.proms.pojos.UserPOJO;
import com.ayush.proms.repo.UserRepo;
import com.ayush.proms.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Integer createUser(UserPOJO userPOJO) {
        return null;
    }

    @Override
    public Integer importUser(MultipartFile file) {
        return null;
    }
}
