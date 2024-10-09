package org.socialculture.platform.member.auth.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.entity.SocialProvider;
import org.socialculture.platform.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // 소셜 로그인 사용자인지 확인
        boolean isSocialLogin = member.getProvider() != SocialProvider.LOCAL;

        // 권한 설정
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().name());

        // 소셜 사용자일 경우 비밀번호를 빈 문자열로 처리하고, 일반 사용자는 실제 비밀번호로 설정
        String password = isSocialLogin ? "" : member.getPassword();

        return new User(member.getEmail(), password, List.of(grantedAuthority));
    }
}
