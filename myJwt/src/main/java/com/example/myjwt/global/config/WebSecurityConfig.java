package com.example.myjwt.global.config;

import com.example.myjwt.auth.jwt.JwtFilter;
import com.example.myjwt.auth.jwt.JwtTokenUtil;
import com.example.myjwt.auth.jwt.handler.CustomAccessDeniedHandler;
import com.example.myjwt.auth.jwt.handler.CustomAuthenticationEntryPoint;
import com.example.myjwt.auth.jwt.handler.JwtExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private static final String[] AUTH_WHITELIST = {"/login", "/members", "/reissue", "/error"};

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                //configurationSource(corsConfigurationSource()).
                //cors 설정 아래의 CorsConfigurationSource 이름을 가진 빈이 있으면 자동 설정
                .csrf().disable()
                //csrf는 stateless에서는 불필요 (쿠키사용하지 않으므로 X)
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                //기본 인증 로그인과 기본 로그인페이지 사용 안하므로
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //jwt기반 stateless 인증을 하므로
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()

                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint()).and()
                .addFilterBefore(new JwtFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionHandlerFilter(), JwtFilter.class)
                .build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers(AUTH_WHITELIST);
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        //모든 origin 허용 (패턴 사용) https://*.*.* 이런것도 가능
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
}
