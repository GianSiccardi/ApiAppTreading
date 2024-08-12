package com.giansiccardi.controllers;

import com.giansiccardi.enums.VerificationType;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.VerificationCode;
import com.giansiccardi.services.CustomerServices;
import com.giansiccardi.services.EmailService;
import com.giansiccardi.services.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {


private final CustomerServices customerServices;
private final EmailService emailService;
private String jwt;
private final VerificationCodeService verificationCodeService;



@GetMapping("/profile")
public ResponseEntity<Customer>getCustomerProfile(@RequestHeader("Authorization") String  jwt) throws Exception {
    Customer customer= customerServices.findCustomerByJwt(jwt);

    return ResponseEntity.status(HttpStatus.OK).body(customer);
}


    @PostMapping("/verification/{verificationType}/send-otp")
    public ResponseEntity<String>sendVerificationOtp(
            @RequestHeader("Authorization") String  jwt,
            @PathVariable VerificationType verificationType) throws Exception {
        Customer customer= customerServices.findCustomerByJwt(jwt);
        VerificationCode verificationCode=verificationCodeService.getVerificationCodeByCustomer(customer.getId());
        if(verificationCode==null){
            verificationCode =verificationCodeService.sendVerificationCOde(customer,verificationType);
        }

        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerifcationOtpEmail(customer.getEmail(),verificationCode.getOtp());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Verification otp enviada exitosamente");
    }

@PatchMapping("/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<?>enableTwoFactorAuthentication(@RequestHeader("Authorization")
                                                                 @PathVariable String otp,
                                                                 String  jwt) throws Exception {
        Customer customer= customerServices.findCustomerByJwt(jwt);
        VerificationCode verificationCode=verificationCodeService.getVerificationCodeByCustomer(customer.getId());
        String sendTo=verificationCode.getVerificationType().equals(VerificationType.EMAIL)?
                verificationCode.getEmail():verificationCode.getMobile();
        boolean isVerified=verificationCode.getOtp().equals(otp);
        if(isVerified){
            Customer customerUpdate=customerServices.enableTwoFactorAuthentication(verificationCode.getVerificationType(),sendTo,customer);
        verificationCodeService.deleteVerificationCodeById(verificationCode);
        return ResponseEntity.status(HttpStatus.OK).body(customerUpdate);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en la autenticacion");
    }
}
