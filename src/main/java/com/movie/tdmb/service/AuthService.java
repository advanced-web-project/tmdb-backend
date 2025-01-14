package com.movie.tdmb.service;

import com.movie.tdmb.dto.*;
import com.movie.tdmb.exception.*;
import com.movie.tdmb.mapper.UserMapper;
import com.movie.tdmb.model.RefreshToken;
import com.movie.tdmb.model.User;
import com.movie.tdmb.repository.RefreshTokenRepository;
import com.movie.tdmb.repository.UserRepository;
import com.movie.tdmb.security.httpclient.OutboundIdentityClient;
import com.movie.tdmb.security.httpclient.OutboundUserClient;
import com.movie.tdmb.security.jwt.JwtUtils;
import com.movie.tdmb.security.service.UserDetailsImpl;
import com.movie.tdmb.util.CloudinaryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final OtpService otpService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    private final CloudinaryUtil cloudinaryUtil;
    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;
    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;
    @NonFinal
    @Value("${redirect-uri}")
    protected String REDIRECT_URI;
    @NonFinal
    protected String GRANT_TYPE = "authorization_code";
    public SignInResponseDto outboundAuthentication(String code) throws Exception {
        // Exchange token using the identity client
        var tokenResponse = outboundIdentityClient.exchangeToken(
                ExchangeTokenRequest.builder()
                        .code(code)
                        .clientId(CLIENT_ID)
                        .clientSecret(CLIENT_SECRET)
                        .redirectUri(REDIRECT_URI)
                        .grantType(GRANT_TYPE)
                        .build()
        );

        // Fetch user info using the outbound user client
        OutboundUserResponse userResponse = outboundUserClient.getUserInfo("json", tokenResponse.getAccessToken());
        String email = userResponse.getEmail();
        String imageUrl = userResponse.getPicture();
        // Check if the user already exists
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            // Create a new user if they don't exist
            return userRepository.save(User.builder()
                    .email(email)
                    .username(userResponse.getName())
                    .password("123456") // Default password
                    .isActive(true)
                    .profile(cloudinaryUtil.uploadImageToCloudinary(imageUrl))
                    .build());
        });
        // Update user profile if necessary
        if (user.getProfile() == null || user.getProfile().isEmpty()) {
            String uploadedImage = cloudinaryUtil.uploadImageToCloudinary(imageUrl);
            user.setProfile(uploadedImage);
            userRepository.save(user);
        }
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Generate JWT and refresh token
        String jwt = jwtUtils.generateJwtToken(authentication);
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
                .filter(token -> token.getExpiryDate().isAfter(Instant.now()))
                .orElseGet(() -> generateRefreshToken(user));
        // Return the response DTO
        return SignInResponseDto.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public User registerUser(SignUpDto signUpDto) {
        if (userRepository.findByUsername(signUpDto.getUsername()).isPresent()) {
            throw new UserNameAlreadyTakenException("Username is already taken");
        }
        if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException("Email is already taken");
        }
        User user = UserMapper.INSTANCE.SignUpDtoToUser(signUpDto);
        user.setCreatedAt(new Date());
        user.setIsActive(false);
        user.setPasswordChangedAt(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public SignInResponseDto login(SignInDto signInDto)
    {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
            RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
                .filter(token -> token.getExpiryDate().isAfter(Instant.now()))
                .orElseGet(() -> generateRefreshToken(user));
            return SignInResponseDto.builder()
                    .accessToken(jwt)
                    .refreshToken(refreshToken.getToken())
                    .user(user)
                    .build();
    }
    public void sendOTP(String email)
    {
       userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Email does not exist in the system!"));
        String otp = emailService.generateOtp();
        otpService.storeOtp(email,otp);
        emailService.sendOtpEmail(email, otp);
    }
    public boolean verifyOTP(String otp,String email) throws Exception
    {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Email does not exist in the system!"));
        boolean valid = otpService.verifyOtp(otp,email);
        if(valid)
        {
            user.setIsActive(true);
            userRepository.save(user);
        }
        return valid;
    }
    public RefreshToken generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(user.getId());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate();
        return refreshTokenRepository.save(refreshToken);
    }
    public String getNewAccessToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));
        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RefreshTokenExpiredException("Refresh token has expired");
        }
        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with refresh token"));
        return jwtUtils.generateTokenFromUsername(user.getUsername());
    }
    public RefreshToken getRefreshTokenFromEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
                .filter(token -> token.getExpiryDate().isAfter(Instant.now()))
                .orElseGet(() -> generateRefreshToken(user));
        return refreshToken;
    }
    public void changePassword(ResetPasswordDto resetPasswordDto) {
        RefreshToken token = refreshTokenRepository.findByToken(resetPasswordDto.getRefreshToken())
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));
        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RefreshTokenExpiredException("Refresh token has expired");
        }
        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with refresh token"));
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        user.setPasswordChangedAt(new Date());
        userRepository.save(user);
    }

}
