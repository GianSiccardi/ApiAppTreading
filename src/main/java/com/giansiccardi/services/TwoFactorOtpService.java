package com.giansiccardi.services;


import com.giansiccardi.models.Customer;
import com.giansiccardi.models.TwoFactorOTP;
import com.giansiccardi.repository.TwoFactorOtpReposity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TwoFactorOtpService {

    private final TwoFactorOtpReposity twoFactorOtpReposity;

public TwoFactorOTP creteTwoFactorOtp(Customer customer,String otp,String jwt){
UUID uuid= UUID.randomUUID();
String ID=uuid.toString();

TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
twoFactorOTP.setOtp(otp);
twoFactorOTP.setJwt(jwt);
twoFactorOTP.setCustomer(customer);



    return twoFactorOtpReposity.save(twoFactorOTP);
}

public TwoFactorOTP findByCustomer(Long id){
return twoFactorOtpReposity.findByCustomerId(id);
}

public TwoFactorOTP findById(String id){
    Optional<TwoFactorOTP>otp=twoFactorOtpReposity.findById(id);
    return otp.orElse(null);
}

public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP ,String otp){


    return twoFactorOTP.getOtp().equals(otp);
}

public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP){
twoFactorOtpReposity.delete(twoFactorOTP);
}
}
