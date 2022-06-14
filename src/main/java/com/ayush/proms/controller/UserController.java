package com.ayush.proms.controller;

import com.ayush.proms.model.User;
import com.ayush.proms.pojos.PasswordChangePojo;
import com.ayush.proms.pojos.UserMinimalDetail;
import com.ayush.proms.pojos.UserPOJO;
import com.ayush.proms.service.UserService;
import com.ayush.proms.utils.BaseController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("user")
@CrossOrigin(origins = "*")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/import-users")
    public ResponseEntity importUsers(@RequestBody MultipartFile file) throws IOException {
        Integer data = userService.importUser(file);
        if (data>=1){
            return new ResponseEntity(successResponse("Users imported Successfully",data), HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse("Failed to import Users",data),HttpStatus.OK);
        }
    }

    @PostMapping("/register")
    public ResponseEntity createUser(@Valid @RequestBody UserPOJO userPOJO){
        Long data = userService.createUser(userPOJO);
        if (data>=1){
            return new ResponseEntity(successResponse("Users created Successfully",data), HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse("Failed to create User",data),HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity allUsers()  {
        List<User> data = userService.getAllUsers();
        if (data!=null){
            return new ResponseEntity(successResponse("Users fetched Successfully",data), HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse("Users fetched Successfully",null),HttpStatus.OK);
        }
    }

    @GetMapping("/current-user-friends")
    public ResponseEntity getCurrentUserFriends()  {
        List<UserMinimalDetail> data = userService.getCurrentUserFriends();
        if (data!=null){
            return new ResponseEntity(successResponse("Users fetched Successfully",data), HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse("Users fetched Successfully",null),HttpStatus.OK);
        }
    }

    @GetMapping("/delete/userId/{userId}")
    public ResponseEntity deleteUserById(@PathVariable("userId") Long userId)  {
        userService.deleteUser(userId);
        return new ResponseEntity(successResponse("Users fetched Successfully",null), HttpStatus.OK);
    }

    @RequestMapping(value = "/type/{type}",method = RequestMethod.GET)
    public ResponseEntity fetchUserByType(@PathVariable("type") String type)  {
        List<UserMinimalDetail> data
                = userService.getUsersByType(type);
        if (data!=null){
            return new ResponseEntity(successResponse("Users fetched Successfully",data), HttpStatus.OK);
        }else {
            return new ResponseEntity(errorResponse("Users fetched Successfully",null),HttpStatus.OK);
        }
    }

    @RequestMapping("/current-user")
    public ResponseEntity fetchCurrentUser(){
        UserPOJO currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(successResponse("Users fetched Successfully",currentUser));
    }

    @GetMapping(value = "/get-user-by-type")
    public ResponseEntity userCountWithType() throws IOException {
        List<Map<String, Integer>> data = userService.getUserCountByType();
        if (data!=null){
            return ResponseEntity.ok(successResponse(customMessageSource.get("crud.get",customMessageSource.get("user")),data));
        }else{
            return new ResponseEntity(errorResponse(customMessageSource.get("not.found",customMessageSource.get("user")),null),HttpStatus.OK);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity changePassword(@RequestBody PasswordChangePojo passwordChangePojo){
        try {
            userService.changePassword(passwordChangePojo);
            return ResponseEntity.ok(successResponse(customMessageSource.get("password.change.success"),null));
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.ok(errorResponse(customMessageSource.get("password.change.failed"),null));
        }
    }


}
