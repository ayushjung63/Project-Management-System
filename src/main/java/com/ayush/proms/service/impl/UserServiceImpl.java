package com.ayush.proms.service.impl;

import com.ayush.proms.enums.Faculty;
import com.ayush.proms.enums.Role;
import com.ayush.proms.enums.Semester;
import com.ayush.proms.mail.Email;
import com.ayush.proms.mail.EmailSender;
import com.ayush.proms.model.User;
import com.ayush.proms.pojos.PasswordChangePojo;
import com.ayush.proms.pojos.UserMinimalDetail;
import com.ayush.proms.pojos.UserPOJO;
import com.ayush.proms.repo.UserRepo;
import com.ayush.proms.service.ExcelService;
import com.ayush.proms.service.UserService;
import com.ayush.proms.utils.AuthenticationUtil;
import com.ayush.proms.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final ExcelService excelService;
    private final AuthenticationUtil authenticationUtil;
    private final EmailSender emailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public UserServiceImpl(UserRepo userRepo, ExcelService excelService, AuthenticationUtil authenticationUtil, EmailSender emailSender) {
        this.userRepo = userRepo;
        this.excelService = excelService;
        this.authenticationUtil = authenticationUtil;
        this.emailSender = emailSender;
    }

    @Override
    public Long createUser(UserPOJO userPOJO) {
        User entity = toEntity(userPOJO);
        String randomPassword = PasswordGenerator.generateRandomPassword();
        entity.setRole(Role.STUDENT);
        entity.setPassword(passwordEncoder.encode(randomPassword));
        User user = userRepo.saveAndFlush(entity);
        if (user!=null){
            userPOJO.setEmail(user.getEmail());
            userPOJO.setPassword(randomPassword);
            sendMail(userPOJO);
            return user.getId();
        }else{
            return 0L;
        }
    }

    @Override
    public Integer importUser(MultipartFile file) throws IOException {
        List<User> userList = excelService.convertToEntity(file.getInputStream());
        if (userList.size() >0 ) {
            List<UserPOJO> pojoList = new ArrayList<>();
            if (userList == null) {
                return 0;
            } else {
                for (User user :
                        userList) {
                    String randomPassword = PasswordGenerator.generateRandomPassword();
                    UserPOJO userPOJO = UserPOJO.builder().email(user.getEmail()).password(randomPassword).build();
                    pojoList.add(userPOJO);
                    user.setRole(Role.STUDENT);
                    user.setPassword(passwordEncoder.encode(randomPassword));
                }
                List<User> users = userRepo.saveAll(userList);
                sendMail(pojoList);
                return 1;
            }
        }else{
            throw new RuntimeException("No data found");
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
                .semester(userPOJO.getSemester()==null ? Semester.NA : userPOJO.getSemester())
                .faculty(userPOJO.getFaculty()==null ? Faculty.NA :userPOJO.getFaculty())
                .address(userPOJO.getAddress())
                .contact(userPOJO.getContact())
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
                .address(user.getAddress())
                .contact(user.getContact())
                .role(user.getRole())
                .passwordChanged(user.isPasswordChanged())
                .build();
    }




    @Override
    public void deleteUser(Long userId) {
        try {
            userRepo.deleteById(userId);
        }catch(Exception e){
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public List<UserMinimalDetail> getUsersByType(String userType) {
        List<UserMinimalDetail> usersByType = userRepo.findUsersByType(userType);
        if (usersByType!=null){
            return usersByType;
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public UserPOJO getCurrentUser() {
        User currentUser = authenticationUtil.getCurrentUser();
        if (currentUser!=null){
            return toPOJO(currentUser);
        }else{
            return new UserPOJO();
        }
    }

    @Override
    public List<Map<String, Integer>> getUserCountByType() {
        List<Map<String, Integer>> userCountByType = userRepo.findUserCountByType();
        return userCountByType;
    }

    @Transactional
    @Override
    public void changePassword(PasswordChangePojo passwordChangePojo) {
        User currentUser = authenticationUtil.getCurrentUser();
        if (Role.ADMIN.equals(currentUser.getRole())){
            User userRepoById = userRepo.getById(passwordChangePojo.getUserId());
            userRepoById.setPassword(passwordEncoder.encode(passwordChangePojo.getNewPassword()));
            userRepoById.setPasswordChanged(true);
            userRepo.save(userRepoById);
        }
        else if (passwordEncoder.matches(passwordChangePojo.getCurrentPassword(),currentUser.getPassword())){
            currentUser.setPassword(passwordEncoder.encode(passwordChangePojo.getNewPassword()));
            currentUser.setPasswordChanged(true);
            userRepo.save(currentUser);
        }else{
            throw new RuntimeException("Please enter correct current password.");
        }
    }

    @Override
    public void sendMail(UserPOJO user) {
        Email email= Email.builder()
                .to(user.getEmail())
                .from("promis.activation@gmail.com")
                .body("Dear "+user.getFullName()+", \n"+
                        "Please find your login credential for the project management system. \n \n "+
                        "Username: "+user.getEmail()+"\n"+ "Password: "+user.getPassword()+
                        "\n \n \n" +
                        "Note: Please do not share your login credential with anyone. Dont forgot to change the default password after you login.\n"+
                        "Thank You!!"
                )
                .subject("Login Credentials")
                .build();
        emailSender.sendLoginCredential(email);
    }

    @Override
    public void sendMail(List<UserPOJO> pojoList) {
        for (UserPOJO user:pojoList) {
            Email email = Email.builder()
                    .to(user.getEmail())
                    .from("promis.activation@gmail.com")
                    .body("Dear " + user.getFullName() + ", \n" +
                            "Please find your login credential for the project management system. \n \n " +
                            "Username: " + user.getEmail() + "\n" + "Password: " + user.getPassword() +
                            "\n \n \n" +
                            "Note: Please do not share your login credential with anyone. Dont forgot to change the default password after you login.\n" +
                            "Thank You!!"
                    )
                    .subject("Project Assigned")
                    .build();
            emailSender.sendLoginCredential(email);
        }
    }
}
