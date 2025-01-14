package com.movie.tdmb.dto;

import com.movie.tdmb.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResponseDto {
    private String accessToken;
    private String refreshToken;
    private User user;
}
