package com.abc.senki.service.implement;

import com.abc.senki.model.entity.CategoryEntity;
import com.abc.senki.repositories.CategoryRepository;
import com.abc.senki.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void saveCategory(CategoryEntity category) {
        categoryRepository.save(category);
    }

    @Override
    public CategoryEntity findById(String cateId) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(UUID.fromString(cateId));
        if (categoryEntity.isEmpty()) {
            return null;
        }
        return categoryEntity.get();
    }

    @Override
    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryEntity> findAllParent() {
        return categoryRepository.findDistinctBySubCategoriesNotNull();
    }

    @Override
    public Boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }


}
