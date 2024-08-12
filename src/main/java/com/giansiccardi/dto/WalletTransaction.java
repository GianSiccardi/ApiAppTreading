package com.giansiccardi.dto;

import com.giansiccardi.enums.WalletTransactionType;
import com.giansiccardi.models.Wallet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Wallet wallet;
    private WalletTransactionType walletTransactionType;
    private LocalDate date;
    private String transferId;
    private String purpose;
    private Long amount;


}
