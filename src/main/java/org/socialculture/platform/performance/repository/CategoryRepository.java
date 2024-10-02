package org.socialculture.platform.performance.repository;

import org.socialculture.platform.performance.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findAllByNameKrIn(List<String> nameKrList);
}
