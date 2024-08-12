package com.giansiccardi.repository;

import com.giansiccardi.models.Customer;
import com.giansiccardi.models.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsDetailsRepository extends JpaRepository<PaymentDetails,Long> {

    PaymentDetails findByCustomerId(Long customerId);


}
