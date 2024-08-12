package com.giansiccardi.repository;

import com.giansiccardi.models.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal,Long> {

List<Withdrawal> findByCustomerId(Long customerId);
}
