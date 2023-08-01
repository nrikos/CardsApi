package com.logicea.cards.api.security.services;

import com.logicea.cards.api.exceptions.CardsApiAuthenticationException;
import com.logicea.cards.api.payloads.CardsApiError;
import com.logicea.cards.api.payloads.JwtTokenResponse;
import com.logicea.cards.api.payloads.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthenticationService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProviderService jwtProviderService;



    public ResponseEntity<?> createAuthToken(LoginRequest loginRequest) throws Exception{
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            final String token = jwtProviderService.generateToken(loginRequest.getEmail(), loginRequest.getPassword(), authorities);
            return ResponseEntity.ok(new JwtTokenResponse(token));
        } catch (DisabledException e) {
            throw new CardsApiAuthenticationException(CardsApiError.EXPIRED_USER);
        } catch (BadCredentialsException e) {
            throw new CardsApiAuthenticationException(CardsApiError.INVALID_CREDENTIALS);
        } catch (AuthenticationException e){
            throw new CardsApiAuthenticationException(CardsApiError.INVALID_CREDENTIALS);
        }
    }

}
