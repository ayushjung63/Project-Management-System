package com.ayush.proms.controller;

import com.ayush.proms.jwt.JwtUtil;
import com.ayush.proms.model.User;
import com.ayush.proms.pojos.JwtResponse;
import com.ayush.proms.pojos.LoginRequest;
import com.ayush.proms.service.impl.UserServiceImpl;
import com.ayush.proms.service.impl.UserServiceImplZ;
import com.ayush.proms.utils.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController extends BaseController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private final UserServiceImplZ userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserServiceImplZ userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/get-token")
    public ResponseEntity createToken(@RequestBody LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
            );
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(errorResponse("Bad Credentials",null),HttpStatus.OK);
        }
        User user = (User) userService.loadUserByUsername(loginRequest.getEmail());
        String jwt = jwtUtil.generateToken(user);
        String role= user.getRole().toString();
        return new ResponseEntity(successResponse("Login successful",new JwtResponse(jwt,role)),HttpStatus.OK);
    }
}