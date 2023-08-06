package com.example.myjwt.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "test_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestMember implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_email")
    @NotNull
    private String memberEmail;

    @Column(name = "member_pw")
//    @NotNull
    private String memberPassword;

    @Column
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserRole> roles = new ArrayList<>();

    @Builder
    public TestMember(String memberEmail, String memberPassword) {
        this.memberEmail = memberEmail;
        this.memberPassword = memberPassword;
        this.roles.add(UserRole.ROLE_ADMIN);
        this.roles.add(UserRole.ROLE_USER);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return memberPassword;
    }

    //얘는 나중에 Authentication 가져올때 이거 사용하는지 확인해보자
    @Override
    public String getUsername() {
        return memberEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
