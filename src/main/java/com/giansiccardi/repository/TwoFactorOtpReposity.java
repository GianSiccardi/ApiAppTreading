package com.giansiccardi.repository;

import com.giansiccardi.models.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwoFactorOtpReposity extends JpaRepository<TwoFactorOTP,String> {

TwoFactorOTP findByCustomerId(Long customerId);
}
