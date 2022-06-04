package com.ayush.proms.service.impl;

import com.ayush.proms.jwt.JwtUtil;
import com.ayush.proms.model.User;
import com.ayush.proms.pojos.UserMinimalDetail;
import com.ayush.proms.pojos.UserPOJO;
import com.ayush.proms.repo.UserRepo;
import com.ayush.proms.service.ExcelService;
import com.ayush.proms.service.UserService;
import com.ayush.proms.utils.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final ExcelService excelService;
    private final AuthenticationUtil authenticationUtil;

    @Autowired
    private  PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepo userRepo, ExcelService excelService, JwtUtil jwtUtil, AuthenticationUtil authenticationUtil) {
        this.userRepo = userRepo;
        this.excelService = excelService;
        this.authenticationUtil = authenticationUtil;
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
            for (User user:
                 userList) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
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
                .email(userPOJO.getEmail())
                .semester(userPOJO.getSemester())
                .faculty(userPOJO.getFaculty())
                .password(passwordEncoder.encode(userPOJO.getPassword()))
                .role(userPOJO.getRole())
                .build();
    }

    @Override
    public List<UserMinimalDetail> getCurrentUserFriends() {
        User currentUser = authenticationUtil.getCurrentUser();
        Optional<List<UserMinimalDetail>> userBySemesterAndFaculty = userRepo.findUserBySemesterAndFaculty(currentUser.getFaculty().toString(), currentUser.getSemester().toString(), currentUser.getId());
        if (userBySemesterAndFaculty.isPresent()){
           return userBySemesterAndFaculty.get();
        }
        return new ArrayList<>();
    }

    @Override
    public UserPOJO toPOJO(User user){
        return UserPOJO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .faculty(user.getFaculty())
                .semester(user.getSemester()==null ? null : user.getSemester())
                .role(user.getRole())
                .build();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return  userRepo.findUserByEmail(username);
    }
}
