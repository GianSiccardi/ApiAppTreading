package com.giansiccardi.services;

import com.giansiccardi.enums.VerificationType;
import com.giansiccardi.models.Customer;
import com.giansiccardi.models.VerificationCode;
import com.giansiccardi.repository.VerificationRepository;
import com.giansiccardi.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    private final VerificationRepository verificationRepository;

    public VerificationCode sendVerificationCOde(Customer customer, VerificationType verificationType){
VerificationCode verificationCode1=new VerificationCode();
verificationCode1.setOtp(OtpUtils.generateOTP());
verificationCode1.setVerificationType(verificationType);
verificationCode1.setCustomer(customer);
return verificationRepository.save(verificationCode1);
    }

    public  VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode>verificationCode=verificationRepository.findById(id);
        if(verificationCode.isPresent()){
            return verificationCode.get();
        }
        throw new Exception("codigo de verificacion no encontrado");
    }

    public  VerificationCode getVerificationCodeByCustomer(Long id){
return verificationRepository.findByCustomerId(id);
    }

    public void deleteVerificationCodeById(VerificationCode verificationCode){
verificationRepository.delete(verificationCode);
    }


}
