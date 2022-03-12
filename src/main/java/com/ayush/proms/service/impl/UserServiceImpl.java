package com.ayush.proms.service.impl;

import com.ayush.proms.model.User;
import com.ayush.proms.pojos.UserPOJO;
import com.ayush.proms.repo.UserRepo;
import com.ayush.proms.service.ExcelService;
import com.ayush.proms.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final ExcelService excelService;

    public UserServiceImpl(UserRepo userRepo, ExcelService excelService) {
        this.userRepo = userRepo;
        this.excelService = excelService;
    }

    @Override
    public Long createUser(UserPOJO userPOJO) {
        User user = userRepo.save(toEntity(userPOJO));
        if (user!=null){
            return user.getId();
        }else{
            return 0L;
        }
    }

    @Override
    public Integer importUser(MultipartFile file) throws IOException {
        List<User> userList = excelService.convertToEntity(file.getInputStream());
        if (userList==null){
            return 0;
        }else {
            List<User> users = userRepo.saveAll(userList);
            return 1;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = userRepo.findAll();
        if (userList==null || userList.isEmpty()){
            return null;
        }else{
            return userList;
        }
    }

    @Override
    public UserPOJO getUserPOJOById(Long studentId) {
        Optional<User> user = userRepo.findById(studentId);
        if (user.isPresent()){
            User user1 = user.get();
           return toPOJO(user1);
        }
        return null;
    }

    @Override
    public User getUserById(Long studentId) {
        Optional<User> user = userRepo.findById(studentId);
        if (user.isPresent()){
            return user.get();
        }else{
            return null;
        }
    }

    @Override
    public Integer saveUser(User user) {
        User data = userRepo.save(user);
        if (data==null){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public User toEntity(UserPOJO userPOJO) {
        return User.builder()
                .id(userPOJO.getId()==null ? null :userPOJO.getId())
                .fullName(userPOJO.getFullName())
                .email(userPOJO.getFullName())
                .semester(userPOJO.getSemester())
                .faculty(userPOJO.getFaculty())
                .password(userPOJO.getPassword())
                .build();
    }

    @Override
    public UserPOJO toPOJO(User user){
        return UserPOJO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .faculty(user.getFaculty())
                .semester(user.getSemester())
                .build();
    }

}
