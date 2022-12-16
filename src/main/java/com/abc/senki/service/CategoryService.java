package com.abc.senki.service;

import com.abc.senki.model.entity.CategoryEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Component
public interface CategoryService {
    void saveCategory(CategoryEntity category);
    CategoryEntity findById(String cateId);
    List<CategoryEntity> findAll();
    List<CategoryEntity> findAllParent();
    Boolean existsByName(String name);
}
