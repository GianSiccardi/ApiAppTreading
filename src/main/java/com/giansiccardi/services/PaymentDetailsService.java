package com.giansiccardi.services;

import com.giansiccardi.models.Customer;
import com.giansiccardi.models.PaymentDetails;
import com.giansiccardi.repository.PaymentsDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentDetailsService {

   private final PaymentsDetailsRepository paymentsDetailsRepository;


   public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String cvu, String bankName, Customer customer){

       PaymentDetails paymentDetails= new PaymentDetails();
       paymentDetails.setAccountHolderName(accountHolderName);
       paymentDetails.setAccountNumber(accountNumber);
       paymentDetails.setCvu(cvu);
       paymentDetails.setBankName(bankName);
       paymentDetails.setCustomer(customer);

       return paymentsDetailsRepository.save(paymentDetails);

    }

    public PaymentDetails getUserPaymentDetails(Customer customer){
return paymentsDetailsRepository.findByCustomerId(customer.getId());
    }
}
