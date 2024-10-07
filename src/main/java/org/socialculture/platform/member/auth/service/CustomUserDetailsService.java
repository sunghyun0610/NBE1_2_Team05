package org.socialculture.platform.member.auth.service;

import lombok.RequiredArgsConstructor;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // MemberRole에서 GrantedAuthority로 변환
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().name());

        return new User(member.getEmail(), member.getPassword(), List.of(grantedAuthority));
//        return memberRepository.findOneWithAuthoritiesByName(email)
//                .map(this::createUser)
//                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

//    private User createUser(MemberEntity member) {
//        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().name());
//
//        return new User(member.getEmail(), member.getPassword(), List.of(grantedAuthority));
//    }
}
