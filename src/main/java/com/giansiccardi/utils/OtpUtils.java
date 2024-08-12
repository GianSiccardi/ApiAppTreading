package com.giansiccardi.utils;

import java.util.Random;

public class OtpUtils {

    public static String generateOTP(){
        int otpLenght=6;
        Random random= new Random();

        StringBuilder otp=new StringBuilder(otpLenght);
        for(int i=0;i<otpLenght;i++){
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
