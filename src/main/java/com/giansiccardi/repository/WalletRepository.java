package com.giansiccardi.repository;

import com.giansiccardi.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

Wallet findByCustomerId(Long customerId);
}
