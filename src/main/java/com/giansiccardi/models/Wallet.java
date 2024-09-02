package com.giansiccardi.models;


import com.giansiccardi.dto.WalletTransaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

@Id
private Long id;
@OneToOne
private Customer customer;

private BigDecimal balance=BigDecimal.ZERO;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WalletTransaction> transactions = new ArrayList<>();



}
