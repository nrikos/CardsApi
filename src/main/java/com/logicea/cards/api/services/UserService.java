package com.logicea.cards.api.services;

import com.logicea.cards.api.dtos.UserDTO;

import java.util.Optional;

public interface UserService {

    UserDTO saveUser(UserDTO userDTO);

    Optional<UserDTO> findUserByEmail(String email);

}
