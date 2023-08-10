package com.example.myjwt.auth.service;

import com.example.myjwt.auth.exception.AuthException;
import com.example.myjwt.auth.exception.AuthExceptionType;
import com.example.myjwt.auth.jwt.JwtTokenUtil;
import com.example.myjwt.auth.jwt.dto.TokenDto;
import com.example.myjwt.auth.jwt.exception.TokenException;
import com.example.myjwt.auth.jwt.exception.TokenExceptionType;
import com.example.myjwt.global.redis.RedisTemplateRepository;
import com.example.myjwt.member.dto.request.MemberRequestDto;
import com.example.myjwt.member.entity.TestMember;
import com.example.myjwt.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisTemplateRepository redisTemplateRepository;

    @Transactional
    public void signUp(MemberRequestDto.SignUp signUp) {
        if (memberRepository.existsByMemberEmail(signUp.getMemberEmail())) {
            throw new AuthException(AuthExceptionType.EMAIL_ALREADY_EXISTS);
        }

        String encryptedPassword = bCryptPasswordEncoder.encode(signUp.getMemberPassword());

        memberRepository.save(
                TestMember.builder()
                        .memberEmail(signUp.getMemberEmail())
                        .memberPassword(encryptedPassword)
                        .build());
    }

    public TokenDto login(MemberRequestDto.Login login, HttpServletResponse response) {
        final TestMember testMember = memberRepository.findByMemberEmail(login.getMemberEmail())
                .orElseThrow(() -> new AuthException(AuthExceptionType.INVALID_EMAIL_OR_PASSWORD));

        if (!bCryptPasswordEncoder.matches(login.getMemberPassword(), testMember.getPassword())) {
            throw new AuthException(AuthExceptionType.INVALID_EMAIL_OR_PASSWORD);
        }

        TokenDto tokenDto = jwtTokenUtil.generateToken(testMember);

        final String refreshToken = tokenDto.getRefreshToken();
        final Long expiration = jwtTokenUtil.getExpiration(refreshToken);
        final Long expirationSecond = expiration / 1000;

        redisTemplateRepository.setDataWithExpiryMillis
                ("RT: " + testMember.getMemberEmail(), refreshToken, expiration);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(expirationSecond.intValue());
//        refreshTokenCookie.setDomain("localhost");
//        refreshTokenCookie.setSecure(true); // HTTPS에서만 전송
//        refreshTokenCookie.setPath("/"); // 경로 설정
        response.addCookie(refreshTokenCookie);

        return tokenDto;
    }

    public String reissue(String refreshToken) {

        Claims claims = jwtTokenUtil.parseRefreshTokenClaims(refreshToken);
        String email = jwtTokenUtil.getEmail(claims);

        String refreshTokenInRedis = redisTemplateRepository.getData("RT: " + email);

        if (refreshTokenInRedis == null || !refreshTokenInRedis.equals(refreshToken)) {
            throw new TokenException(TokenExceptionType.INVALID_REFRESH_TOKEN);
        }

        final TestMember testMember = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new TokenException(TokenExceptionType.INVALID_REFRESH_TOKEN));

        return jwtTokenUtil.reissueAccessToken(testMember);
    }

    public void logout(String refreshToken) {

        Claims claims = jwtTokenUtil.parseRefreshTokenClaims(refreshToken);
        String email = jwtTokenUtil.getEmail(claims);

        redisTemplateRepository.deleteData("RT: " + email);

    }
}
