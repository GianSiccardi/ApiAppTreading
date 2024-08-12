package com.giansiccardi.controllers;

import com.giansiccardi.models.Customer;
import com.giansiccardi.models.PaymentDetails;
import com.giansiccardi.services.CustomerServices;
import com.giansiccardi.services.PaymentDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymentDetails")
@RequiredArgsConstructor
public class PaymentDetailsController {
private final CustomerServices customerServices;
private final PaymentDetailsService paymentDetailsService;



@PostMapping
    public ResponseEntity<PaymentDetails>addPaymentDetails(
            @RequestBody PaymentDetails paymentDetailsRequest,
            @RequestHeader("Authorization") String jwt
) throws Exception {
    Customer customer=customerServices.findCustomerByJwt(jwt);

    PaymentDetails paymentDetails=paymentDetailsService.addPaymentDetails(
            paymentDetailsRequest.getAccountNumber(),
            paymentDetailsRequest.getAccountHolderName(),
            paymentDetailsRequest.getIfsc(),
            paymentDetailsRequest.getBankName(),
            customer
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(paymentDetails);
}

@GetMapping
public ResponseEntity<PaymentDetails>getUserPaymentDetails(
        @RequestHeader("Authorization") String jwt
) throws Exception {
    Customer customer=customerServices.findCustomerByJwt(jwt);

    PaymentDetails paymentDetails=paymentDetailsService.getUserPaymentDetails(customer);
    return ResponseEntity.ok().body(paymentDetails);
}
}
