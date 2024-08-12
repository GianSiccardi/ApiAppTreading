package com.giansiccardi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.giansiccardi.enums.USER_ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
private String fullName;
private String email;

@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
private String password;

@Embedded
private TwoFactorAuth twoFactorAuth= new TwoFactorAuth();

private USER_ROLE role =USER_ROLE.USER;

}
