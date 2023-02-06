package com.abc.senki.mapping;

import com.abc.senki.model.entity.CategoryEntity;
import com.abc.senki.model.entity.ProductEntity;
import com.abc.senki.model.payload.request.ProductRequest.AddNewProductRequest;
import com.abc.senki.repositories.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductMapping {

    public static ProductEntity toEntity(AddNewProductRequest request, CategoryEntity category){
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(request.getName());

        productEntity.setDescription(request.getDescription());
        productEntity.setPrice(request.getPrice());
        productEntity.setStock(request.getStock());
        productEntity.setProductCategory(category);
        productEntity.setId(UUID.randomUUID());
        productEntity.setCreatedAt(LocalDateTime.now());

        return productEntity;
    }
}
