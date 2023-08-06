package com.example.myjwt.jwt.handler;

import com.example.myjwt.jwt.exception.TokenException;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            log.error("--------------------------------");
            log.error("StackTrace = {} ", (Object) e.getStackTrace());
            log.error("HttpStatus = {} ", e.getCustomExceptionType().getHttpStatus());
            log.error("ErrorMsg = {} ", e.getCustomExceptionType().getErrorMsg());
            log.error("--------------------------------");

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setStatus(e.getCustomExceptionType().getHttpStatus().value());
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", e.getCustomExceptionType().getErrorMsg());
            response.getWriter().print(responseJson);
        }
    }
}
