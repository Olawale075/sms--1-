package sms.com.sms.service;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final String DIGITS = "0123456789";

    public static String generateOtp(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return otp.toString();
    }
}
