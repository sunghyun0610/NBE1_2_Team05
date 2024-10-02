package org.socialculture.platform.performance.dto;

import lombok.Builder;

@Builder
public record CategoryContent(Long categoryId, String nameKr, String nameEn) {
    public static CategoryContent of (Long categoryId, String nameKr, String nameEn) {
        return CategoryContent.builder()
                .categoryId(categoryId)
                .nameKr(nameKr)
                .nameEn(nameEn)
                .build();
    }
}
