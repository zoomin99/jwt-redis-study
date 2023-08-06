package com.example.myjwt.member.dto.request;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

public class MemberRequestDto {

    @Getter
    public static class SignUp {


        //@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String memberEmail;

        private String memberPassword;
    }

    @Getter
    public static class Login {


        //@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String memberEmail;

        private String memberPassword;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(memberEmail, memberPassword);
        }
    }

    @Getter
    public static class Access {
        private String email;
        private List<String> roles = new ArrayList<>();

        private Access(String email, List<String> authorities) {
            this.email = email;
            roles.addAll(authorities);
        }

        public static Access from(String email, List<String> authorities) {
            return new Access(email, authorities);
        }
    }
}
