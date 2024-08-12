package com.giansiccardi.controllers;


import com.giansiccardi.dto.PaymentResponse;
import com.giansiccardi.enums.PaymentMethod;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.PaymentOrder;
import com.giansiccardi.services.CustomerServices;
import com.giansiccardi.services.PaymentOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentOrderController {

private final CustomerServices customerServices;

private final PaymentOrderService paymentOrderService;


@PostMapping("/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse>paymenHandler(
        @PathVariable PaymentMethod paymentMethod,
        @PathVariable Long amount,
        @RequestHeader("Authorization")String jwt
) throws Exception {
    Customer customer=customerServices.findCustomerByJwt(jwt);

    PaymentResponse paymentResponse;

    PaymentOrder order= paymentOrderService.createOrder(customer,amount,paymentMethod);
    if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
        paymentResponse=paymentOrderService.createPaypalPaymentLink(customer,amount);
    }else {
        paymentResponse=paymentOrderService.createStripePaymentLink(customer,amount, order.getId());
    }

    return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
}
}
