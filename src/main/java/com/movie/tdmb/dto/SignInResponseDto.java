package com.movie.tdmb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInResponseDto {
    private String accessToken;
    private String refreshToken;
}
