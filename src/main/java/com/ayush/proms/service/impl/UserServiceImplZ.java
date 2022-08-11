package com.ayush.proms.service.impl;

import com.ayush.proms.model.User;
import com.ayush.proms.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplZ implements UserDetailsService {
    private final UserRepo userRepo;

    public UserServiceImplZ(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(username).get();
        return user;
    }
}
