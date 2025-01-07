package com.movie.tdmb.controller;
import com.movie.tdmb.dto.SignInDto;
import com.movie.tdmb.dto.SignUpDto;
import com.movie.tdmb.dto.VerifyOtpDto;
import com.movie.tdmb.model.RefreshToken;
import com.movie.tdmb.model.User;
import com.movie.tdmb.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;
    /**
     * Register a new user
     * @param signUpDto
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody SignUpDto signUpDto) {
        User registeredUser = authService.registerUser(signUpDto);
        authService.sendOTP(signUpDto.getEmail());
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
    /**
     * Login a user
     * @param signInDto
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody SignInDto signInDto) {
        return new ResponseEntity<>(authService.login(signInDto), HttpStatus.CREATED);
    }
    /**
     * Sends an OTP to an email.
     *
     * @param email the email to send the OTP to
     * @return a ResponseEntity containing the result of the operation
     */
    @GetMapping("/get-otp")
    public ResponseEntity<?> getOtp(@RequestParam String email) throws Exception {
        authService.sendOTP(email);
        return ResponseEntity.ok("OTP sent successfully");
    }
    /**
     * Verifies an OTP.
     *
     * @param request the VerifyOtpDto containing the OTP and email
     * @return a ResponseEntity containing the result of the verification
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpDto request) throws Exception {
        boolean isValidOtp = authService.verifyOTP(request.getOtp(),request.getEmail());
        if (!isValidOtp) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP is wrong");
        }
        return ResponseEntity.ok("Account active successfully");
    }
    /**
     * Refreshes the access token.
     *
     * @param request the request containing the refresh token
     * @return a ResponseEntity containing the new access token
     */
    @PostMapping("refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String newAccessToken = authService.getNewAccessToken(refreshToken);
        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }
    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyOtpChangePassword(@RequestBody VerifyOtpDto request) throws Exception {
        boolean isValidOtp = authService.verifyOTP(request.getOtp(),request.getEmail());
        if (!isValidOtp) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP is wrong");
        }
        RefreshToken refreshToken = authService.getRefreshTokenFromEmail(request.getEmail());
        return ResponseEntity.ok(refreshToken);
    }
}
