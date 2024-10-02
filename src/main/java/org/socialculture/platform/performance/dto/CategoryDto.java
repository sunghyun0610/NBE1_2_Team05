package org.socialculture.platform.performance.dto;

import lombok.Builder;
import org.socialculture.platform.performance.dto.domain.CategoryContent;
import org.socialculture.platform.performance.entity.CategoryEntity;

@Builder
public record CategoryDto(Long categoryId, String nameKr, String nameEn) {
    public static CategoryDto of (Long categoryId, String nameKr, String nameEn) {
        return CategoryDto.builder()
                .categoryId(categoryId)
                .nameKr(nameKr)
                .nameEn(nameEn)
                .build();
    }

    public static CategoryDto toDto (CategoryContent categoryContent) {
        return CategoryDto.builder()
                .categoryId(categoryContent.categoryId())
                .nameKr(categoryContent.nameKr())
                .nameEn(categoryContent.nameEn())
                .build();
    }

    public static CategoryDto toDto (CategoryEntity categoryEntity) {
        return CategoryDto.builder()
                .categoryId(categoryEntity.getCategoryId())
                .nameKr(categoryEntity.getNameKr())
                .nameEn(categoryEntity.getNameEn())
                .build();
    }
}