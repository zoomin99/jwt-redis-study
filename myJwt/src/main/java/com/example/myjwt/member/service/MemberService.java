package com.example.myjwt.member.service;

import com.example.myjwt.auth.exception.AuthException;
import com.example.myjwt.auth.exception.AuthExceptionType;
import com.example.myjwt.dto.TokenDto;
import com.example.myjwt.jwt.JwtTokenUtil;
import com.example.myjwt.member.dto.request.MemberRequestDto;
import com.example.myjwt.member.entity.TestMember;
import com.example.myjwt.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final JwtTokenUtil jwtTokenUtil;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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

    public TokenDto login(MemberRequestDto.Login login) {
        final TestMember testMember = memberRepository.findByMemberEmail(login.getMemberEmail())
                .orElseThrow(() -> new AuthException(AuthExceptionType.INVALID_EMAIL_OR_PASSWORD));

        if (!bCryptPasswordEncoder.matches(login.getMemberPassword(), testMember.getPassword())) {
            throw new AuthException(AuthExceptionType.INVALID_EMAIL_OR_PASSWORD);
        }

//        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(testMember);
    }
}
