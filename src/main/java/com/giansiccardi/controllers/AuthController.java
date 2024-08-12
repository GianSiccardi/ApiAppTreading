package com.giansiccardi.controllers;

import com.giansiccardi.config.JwtProvider;
import com.giansiccardi.dto.AuthResponse;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.TwoFactorOTP;
import com.giansiccardi.repository.CustomerRepository;
import com.giansiccardi.services.CustomerServices;
import com.giansiccardi.services.EmailService;
import com.giansiccardi.services.TwoFactorOtpService;
import com.giansiccardi.services.WatchListService;
import com.giansiccardi.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CustomerServices  customerServices;

    private final TwoFactorOtpService twoFactorOtpService;

    private final EmailService emailService;

    private final WatchListService watchListService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse>register (@RequestBody  Customer customer) throws Exception {

        Customer customerRegister=customerServices.createCustomer(customer);

        watchListService.createWatchList(customerRegister);
        Authentication authentication= new UsernamePasswordAuthenticationToken(
                customer.getEmail(),
                customer.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse authResponse= new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("Registro exitoso");

return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login (@RequestBody  Customer customer) throws Exception {
    String email=customer.getEmail();
    String password=customer.getPassword();

    Authentication auth= customerServices.authentication(email, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        Customer authCustomer=customerServices.findByEmail(customer.getEmail());
        if(customer.getTwoFactorAuth().isEnabled()){
            AuthResponse authResponse= new AuthResponse();
            authResponse.setMessage("Two factor auth is enabled");
            authResponse.setTwoFactorAuthEnabled(true);
            String otp= OtpUtils.generateOTP();
            TwoFactorOTP oldTwoFactorOtp=twoFactorOtpService.findByCustomer(authCustomer.getId());
            if(oldTwoFactorOtp!=null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }
            TwoFactorOTP newTwoFactorOtp=twoFactorOtpService.creteTwoFactorOtp(authCustomer,otp,jwt);
            emailService.sendVerifcationOtpEmail(email,otp);
            authResponse.setSession(newTwoFactorOtp.getId());
            return new ResponseEntity<>(authResponse,HttpStatus.ACCEPTED);
        }
        AuthResponse authResponse= new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("Login exitoso");

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

    }
@PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<?>verifyLoginOtp
            (@PathVariable String otp,
             @RequestParam String id){
          TwoFactorOTP twoFactorOTP=twoFactorOtpService.findById(id);
          if (twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP,otp)){
              AuthResponse authResponse= new AuthResponse();
              authResponse.setMessage("tWO FACTOR AUTHENTICATION VERIFICADO");
              authResponse.setTwoFactorAuthEnabled(true);
              authResponse.setJwt(twoFactorOTP.getJwt());
              return new ResponseEntity<>(authResponse,HttpStatus.ACCEPTED);
          }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("otp invalido");
    }
}
