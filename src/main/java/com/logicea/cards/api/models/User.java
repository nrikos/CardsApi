package com.logicea.cards.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logicea.cards.api.payloads.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(nullable = false,unique = true)
    private String email;
    @Column
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    Role role;


}
