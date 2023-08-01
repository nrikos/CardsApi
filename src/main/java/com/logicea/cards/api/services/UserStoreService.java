package com.logicea.cards.api.services;

import com.logicea.cards.api.dtos.UserDTO;

import java.util.Optional;

public interface UserStoreService {

    UserDTO saveUser(UserDTO user);

   Optional<UserDTO> findByEmail(String email);

}
