package com.movie.tdmb.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewAccessTokenDto {
    @NotBlank(message = "Refresh token is mandatory")
    private String refreshToken;
}
