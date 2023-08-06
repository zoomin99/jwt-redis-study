package com.example.myjwt.jwt.handler;

import com.google.gson.JsonObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 스프링에서 제공하는 ExceptionHandler는 ControllerAdvice
 * 즉 컨트롤러 단계에서 적용되므로 필터단계에서는 적용되지 않음
 * 따라서 직접 ExceptionHandler를 구현해야돼서 만든 핸들러
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("message", "접근 권한이 없습니다");
        response.getWriter().print(responseJson);
    }
}
