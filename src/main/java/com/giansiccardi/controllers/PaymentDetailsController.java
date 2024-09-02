package com.giansiccardi.controllers;

import com.giansiccardi.models.Customer;
import com.giansiccardi.models.PaymentDetails;
import com.giansiccardi.services.CustomerServices;
import com.giansiccardi.services.PaymentDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ResponseEntity<?>addPaymentDetails(
            @RequestBody PaymentDetails paymentDetailsRequest,
            @RequestHeader("Authorization") String jwt
) throws Exception {

   try{ Customer customer=customerServices.findCustomerByJwt(jwt);

    PaymentDetails paymentDetails=paymentDetailsService.addPaymentDetails(
            paymentDetailsRequest.getAccountNumber(),
            paymentDetailsRequest.getAccountHolderName(),
            paymentDetailsRequest.getCvu(),
            paymentDetailsRequest.getBankName(),
            customer
    );
       return ResponseEntity.status(HttpStatus.CREATED).body(paymentDetails);
   }  catch (DataIntegrityViolationException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Detalles de pago duplicados, no se puede insertar el registro.");
    } catch (Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(" Otro tipo de error");
    }

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
