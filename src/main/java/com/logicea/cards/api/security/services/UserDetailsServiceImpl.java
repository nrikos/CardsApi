package com.logicea.cards.api.security.services;

import com.logicea.cards.api.dtos.UserDTO;
import com.logicea.cards.api.services.UserStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserStoreService userStoreService;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserDTO> user = userStoreService.findByEmail(email);
        return user.map(u -> new User(u.getEmail(), u.getPassword(), Set.of(new SimpleGrantedAuthority(String.format("ROLE_%s",u.getUserRole().name())))))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
    }
}
