package com.example.myjwt.auth.controller;

import com.example.myjwt.auth.jwt.dto.TokenDto;
import com.example.myjwt.auth.jwt.exception.TokenException;
import com.example.myjwt.auth.jwt.exception.TokenExceptionType;
import com.example.myjwt.auth.service.AuthService;
import com.example.myjwt.member.dto.request.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/members")
    public ResponseEntity<String> signUp(@RequestBody MemberRequestDto.SignUp signUp) {
        authService.signUp(signUp);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto.Login login, HttpServletResponse response) {

        TokenDto tokenDto = authService.login(login, response);

        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(
            @CookieValue(value = "refreshToken", required = false) Cookie refreshCookie) {
        if (refreshCookie == null) {
            throw new TokenException(TokenExceptionType.REFRESH_TOKEN_NOT_EXIST);
        }
        String refreshToken = refreshCookie.getValue();
        String reissuedAccessToken = authService.reissue(refreshToken);
        return ResponseEntity.ok(reissuedAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @CookieValue(value = "refreshToken", required = false) Cookie refreshCookie) {
        if (refreshCookie == null) {
            throw new TokenException(TokenExceptionType.REFRESH_TOKEN_NOT_EXIST);
        }
        String refreshToken = refreshCookie.getValue();

        authService.logout(refreshToken);

        refreshCookie.setMaxAge(0);
        return ResponseEntity.ok(null);
    }
}
