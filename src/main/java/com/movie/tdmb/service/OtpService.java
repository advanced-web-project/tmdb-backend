package com.movie.tdmb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final StringRedisTemplate redisTemplate;
    private static final long OTP_EXPIRATION_MINUTES = 5;
    public void storeOtp(String email, String otp) {
        redisTemplate.opsForValue().set("OTP_" + email, otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }
    public boolean verifyOtp(String otp,String email) {
        String storedOtp = redisTemplate.opsForValue().get("OTP_"+email);
        return otp.equals(storedOtp);
    }
}
