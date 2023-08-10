package com.example.myjwt.auth.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}
