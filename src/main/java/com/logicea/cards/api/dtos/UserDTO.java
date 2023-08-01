package com.logicea.cards.api.dtos;

import com.logicea.cards.api.payloads.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Email
    private String email;
    private String password;
    private Role userRole;
    private List<CardDTO> cardList;
}
