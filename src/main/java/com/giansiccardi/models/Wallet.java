package com.giansiccardi.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
@OneToOne
private Customer customer;

private BigDecimal balance=BigDecimal.ZERO;



}
