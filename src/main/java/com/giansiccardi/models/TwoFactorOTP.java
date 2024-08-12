package com.giansiccardi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoFactorOTP {

    @Id

    private String id;

    private String otp;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne
    private Customer customer;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jwt;
}
