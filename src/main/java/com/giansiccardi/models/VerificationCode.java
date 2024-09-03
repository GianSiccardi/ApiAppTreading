package com.giansiccardi.models;

import com.giansiccardi.enums.VerificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String otp;
@OneToOne
    private Customer customer;

private String email;
private String mobile;
private VerificationType verificationType;
}
