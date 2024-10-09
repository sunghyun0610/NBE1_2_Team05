package org.socialculture.platform.member.auth.service;


import org.socialculture.platform.member.auth.dto.TokenResponseDTO;

public interface AuthService {
    boolean validateRefreshToken(String refreshToken);
    String createNewAccessToken(String refreshToken);
    void insertRefreshToken(String refreshToken);
    TokenResponseDTO createTokenResponseForSocialMember(String email);
}