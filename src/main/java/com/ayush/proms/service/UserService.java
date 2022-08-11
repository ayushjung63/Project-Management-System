package com.ayush.proms.service;

import com.ayush.proms.mail.Email;
import com.ayush.proms.model.User;
import com.ayush.proms.pojos.MinimalDetail;
import com.ayush.proms.pojos.PasswordChangePojo;
import com.ayush.proms.pojos.UserMinimalDetail;
import com.ayush.proms.pojos.UserPOJO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public interface UserService {

    Long createUser(UserPOJO userPOJO);
    Integer saveUser(User user);

    Integer importUser(MultipartFile file) throws IOException;

    List<User> getAllUsers();
    UserPOJO getUserPOJOById(Long studentId);
    User getUserById(Long studentId);

    public UserPOJO toPOJO(User user);
    public User toEntity(UserPOJO userPOJO);

    List<UserMinimalDetail> getCurrentUserFriends();

    void deleteUser(Long userId);

    List<UserMinimalDetail> getUsersByType(String userType);

    UserPOJO getCurrentUser();

    List<Map<String, Integer>> getUserCountByType();

    void changePassword(PasswordChangePojo passwordChangePojo);

    void sendMail(UserPOJO user);

    void sendMail(List<UserPOJO> user);
}
