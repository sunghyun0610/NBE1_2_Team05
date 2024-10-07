package org.socialculture.platform.member.dto.response;

import org.socialculture.platform.performance.entity.CategoryEntity;

public record CategoryResponse(
        Long categoryId,
        String nameKr,
        String nameEn
) {


    public static CategoryResponse fromEntity(CategoryEntity categoryEntity) {
        return new CategoryResponse(
                categoryEntity.getCategoryId(),
                categoryEntity.getNameKr(),
                categoryEntity.getNameEn()
        );
    }


}
