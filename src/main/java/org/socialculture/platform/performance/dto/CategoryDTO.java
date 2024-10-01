package org.socialculture.platform.performance.dto;

import lombok.Builder;

@Builder
public record CategoryDTO(Long categoryId, String nameKr, String nameEn) {
    public static CategoryDTO of (Long categoryId, String nameKr, String nameEn) {
        return CategoryDTO.builder()
                .categoryId(categoryId)
                .nameKr(nameKr)
                .nameEn(nameEn)
                .build();
    }
}
