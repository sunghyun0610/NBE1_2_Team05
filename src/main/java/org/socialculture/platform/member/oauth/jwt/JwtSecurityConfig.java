package org.socialculture.platform.member.oauth.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * jwt 인증을 위한 필터를 SecurityConfig의 HTTP 보안 설정에 추가
 * @author yeonsu
 */
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * HttpSecurity 객체를 받아 jwt 필터를 추출
     * @param http
     */
    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(
                new JwtFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class
        );
    }
}
