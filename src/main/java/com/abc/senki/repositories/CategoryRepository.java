package com.abc.senki.repositories;

import com.abc.senki.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findById(UUID cateId);
    List<CategoryEntity> findDistinctBySubCategoriesNotNull();
    Boolean existsByName(String name);
}
