package com.logicea.cards.api.services;

import com.logicea.cards.api.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserStoreService userStoreService;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;


    public Optional<UserDTO> findUserByEmail(String email){
        return userStoreService.findByEmail(email);
    }


    @Override
    public UserDTO saveUser(UserDTO user) {
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userStoreService.saveUser(user);
    }
}
