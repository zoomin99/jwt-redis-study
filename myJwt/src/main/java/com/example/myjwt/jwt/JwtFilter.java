package com.example.myjwt.jwt;

import com.example.myjwt.jwt.exception.TokenException;
import com.example.myjwt.jwt.exception.TokenExceptionType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER_TYPE_PREFIX = "Bearer ";
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String accessToken = extractToken(authenticationHeader);
        Claims claims = jwtTokenUtil.parseClaims(accessToken);
        //여기서 유효성도 같이 검사 (기간, 싸인 등등)
        Authentication authentication = jwtTokenUtil.getAuthentication(claims);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String extractToken(String authenticationHeader) {
        if (authenticationHeader == null) {
            throw new TokenException(TokenExceptionType.ACCESS_TOKEN_NOT_EXIST);
        } else if (!authenticationHeader.startsWith(BEARER_TYPE_PREFIX)) {
            throw new TokenException(TokenExceptionType.INVALID_TOKEN);
        }
        return authenticationHeader.substring(BEARER_TYPE_PREFIX.length());
    }
}
