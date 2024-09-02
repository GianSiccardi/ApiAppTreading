package com.giansiccardi.repository;

import com.giansiccardi.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    @Query("SELECT w FROM Wallet w LEFT JOIN FETCH w.transactions WHERE w.customer.id = :customerId")
    Wallet findByCustomerId(Long customerId);
}
