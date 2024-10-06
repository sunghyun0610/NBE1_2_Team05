package org.socialculture.platform.member.auth.service;

public interface AuthService {
    boolean validateRefreshToken(String refreshToken);
    String createNewAccessToken(String refreshToken);
}