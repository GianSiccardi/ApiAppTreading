package com.giansiccardi.repository;

import com.giansiccardi.models.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationCode,Long> {


public VerificationCode findByCustomerId(Long id);
}
