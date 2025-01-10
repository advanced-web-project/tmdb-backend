package com.movie.tdmb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResetPasswordDto {
    private String password;
    private String refreshToken;
}
