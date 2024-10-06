package org.socialculture.platform.member.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String message;

    public TokenResponseDTO(String message) {
        this.message = message;
    }
}
