package org.socialculture.platform.performance.repository;

import org.socialculture.platform.performance.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findAllByNameKrIn(List<String> nameKrList);

    Optional<CategoryEntity> findByCategoryId(Long id);
}
