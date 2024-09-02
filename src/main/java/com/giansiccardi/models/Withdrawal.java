package com.giansiccardi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.giansiccardi.enums.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Withdrawal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private WithdrawalStatus withdrawalStatus;

    private Long amount;


    @ManyToOne
    private Customer customer;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime=LocalDateTime.now();



}
