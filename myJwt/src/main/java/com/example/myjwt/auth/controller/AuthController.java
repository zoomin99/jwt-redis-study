package com.example.myjwt.auth.controller;

import com.example.myjwt.dto.TokenDto;
import com.example.myjwt.jwt.JwtTokenUtil;
import com.example.myjwt.member.dto.request.MemberRequestDto;
import com.example.myjwt.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/members")
    public ResponseEntity<String> signUp(@RequestBody MemberRequestDto.SignUp signUp) {
        memberService.signUp(signUp);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto.Login login) {

        TokenDto tokenDto = memberService.login(login);

        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/hi")
    public String hi(@AuthenticationPrincipal MemberRequestDto.Access access) {
        return access.getRoles().toString();
    }
}
