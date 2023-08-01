package com.logicea.cards.api.services;

import com.logicea.cards.api.dtos.UserDTO;
import com.logicea.cards.api.models.User;
import com.logicea.cards.api.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserStoreServiceImpl implements UserStoreService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public UserDTO saveUser(UserDTO user) {
        return modelMapper.map(userRepository.save(modelMapper.map(user, User.class)),UserDTO.class);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
       return userRepository.findDistinctByEmail(email).map(u -> modelMapper.map(u, UserDTO.class));
    }
}
